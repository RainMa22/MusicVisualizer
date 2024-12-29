package me.rainma22.intermediateimage.Renderers;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.UnsupportedAudioFileException;
import me.rainma22.intermediateimage.ColorProvider;
import me.rainma22.intermediateimage.ContainerComponent;
import me.rainma22.intermediateimage.EffectApplier;
import me.rainma22.intermediateimage.Effects.ContainerEffects.BackgroundImage;
import me.rainma22.intermediateimage.Effects.PolylineEffects.TransformByAudio;
import me.rainma22.intermediateimage.PolyLine;
import me.rainma22.intermediateimage.Resources.Audio;
import me.rainma22.intermediateimage.Resources.Image;
import me.rainma22.intermediateimage.Resources.ResourceManager;
import me.rainma22.musicvisualizer.MusicExtractor;
import me.rainma22.musicvisualizer.util.BinaryOperations;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.apache.commons.math3.util.FastMath;

/**
 * represent a EffectApplier implemented using System's audio library for audio
 * and AWT for image;
 */
public class SystemEffectApplier extends EffectApplier {

    private ResourceLoader loader;
    private static HashMap<ResourceManager, SystemEffectApplier> appliers;

    private SystemEffectApplier(ResourceManager resMan) {
        super(resMan);
        loader = new ResourceLoader(resMan);
    }

    public static SystemEffectApplier of(ResourceManager resMan) {
        if (!appliers.containsKey(resMan)) {
            appliers.put(resMan, new SystemEffectApplier(resMan));
        }
        return appliers.get(resMan);
    }

    public ResourceLoader getResourceLoader() {
        return loader;
    }

    @Override
    public ContainerComponent applyBackgroundImage(int currentFrame, BackgroundImage bi) {
        ContainerComponent target = bi.getTarget();
        if (bi.getResourceIds().isEmpty()) {
            return target.copy();
        }
        String imageId = bi.getResourceIds().getFirst();
        Image image = resMan.getImage(imageId);
        if (image == null) {
            return target.copy();
        }

        ContainerComponent result = target.copy();

        result.setBackgroundColor_rgba(ColorProvider.ofImage(image));
        return result;

    }

    @Override
    public PolyLine applyTransformByAudio(int currentFrame, TransformByAudio tba) {
        PolyLine target = tba.getTarget();
        if (tba.getResourceIds().isEmpty()) {
            return target.copy();
        }
        String audioId = tba.getResourceIds().getFirst();
        double[] data = null;
        try {
            data = getResourceLoader().loadAudio(audioId);
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(SystemEffectApplier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SystemEffectApplier.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (data == null) {
            return target.copy();
        }
        
        FastFourierTransformer transformer = tba.getTransformer();
        double[] subData = Arrays.copyOfRange(data, currentFrame*target.size(),
                (currentFrame+1)*target.size());
        subData = Arrays.copyOf(subData, 
                BinaryOperations.nextPowerOfTwo(currentFrame));
        Complex[] transformedData = transformer.transform(subData, TransformType.FORWARD);

        Float[] amp = new Float[target.size()];
        for (int i = 0; i < amp.length; i++) {
            double real = transformedData[i].getReal();
            double imaginary = transformedData[i].getImaginary();
            amp[i] = (float) FastMath.log(FastMath.sqrt(real*real + imaginary*imaginary) + 1);
        }

        return target.transform(Arrays.asList(amp));
    
    }

    private class ResourceLoader {

        private HashMap<String, java.awt.Image> cachedImages = new HashMap<>();
        private HashMap<String, double[]> cachedAudios = new HashMap<>();
        private ResourceManager resMan;

        public ResourceLoader(ResourceManager resMan) {
            this.resMan = resMan;
        }

        public double[] loadAudio(String resId) throws UnsupportedAudioFileException, IOException {
            Audio audio = resMan.getAudio(resId);
            if (audio == null) {
                cachedAudios.put(resId, null);
            }
            if (!cachedAudios.containsKey(resId)) {
                MusicExtractor me = new MusicExtractor(audio.getPath().toString());
                double[] data = me.readFile();
                cachedAudios.put(resId, data);
            }

            return cachedAudios.get(resId);

        }

        public java.awt.Image loadImage(String resId) throws IOException {
            Image image = resMan.getImage(resId);
            if (image == null) {
                cachedImages.put(resId, null);
            }
            if (!cachedImages.containsKey(resId)) {
                java.awt.Image result = ImageIO.read(image.getPath().toFile());
                cachedImages.put(resId, result);
            }
            return cachedImages.get(resId);
        }

    }

}
