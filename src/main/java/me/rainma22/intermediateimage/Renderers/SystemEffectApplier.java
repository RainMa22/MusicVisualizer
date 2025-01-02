package me.rainma22.intermediateimage.Renderers;

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

import javax.imageio.ImageIO;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * represent a EffectApplier implemented using System's audio library for audio
 * and AWT for image;
 */
public class SystemEffectApplier extends EffectApplier {

    private ResourceLoader loader;
    private static HashMap<ResourceManager, SystemEffectApplier> appliers = new HashMap<>();

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
        String imageId = bi.getImageId();
        if (imageId == null) {
            return target.copy();
        }
        Image image = resMan.getImage(imageId);
        if (image == null) {
            return (ContainerComponent) target.copy();
        }

        ContainerComponent result = (ContainerComponent) target.copy();

        result.setBackgroundColorProvider(ColorProvider.ofImage(image));
        return result;

    }

    @Override
    public PolyLine applyTransformByAudio(int currentFrame, TransformByAudio tba) {
        PolyLine target = tba.getTarget();
        String audioId = tba.getAudioId();
        String scaleId = tba.getScaleId();
        String strokeSizeId = tba.getStrokeSizeId();
        int scale = 50;
        int strokeSize = 1;
        if(scaleId != null) ;// load from resMan;
        if(strokeSizeId != null) ;// load from resMan;

        PolyLine result = target.copy();
        result.setStrokeSize_px(strokeSize);
        if(audioId == null){
            return result;
        }
        Audio audio = resMan.getAudio(audioId);
        if (audio == null) return result;

        double[] data = null;
        try {
            data = getResourceLoader().loadAudio(audio);
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(SystemEffectApplier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SystemEffectApplier.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (data == null) {
            return result;
        }
        FastFourierTransformer transformer = tba.getTransformer();
        double[] subData = Arrays.copyOfRange(data, currentFrame*target.size(),
                (currentFrame+1)*target.size());
        subData = Arrays.copyOf(subData, 
                BinaryOperations.nextPowerOfTwo(target.size()));

        Complex[] transformedData = transformer.transform(subData, TransformType.FORWARD);

        Float[] amp = new Float[target.size()];
        for (int i = 0; i < amp.length; i++) {
            double real = transformedData[i].getReal();
            double imaginary = transformedData[i].getImaginary();
            amp[i] = (float) FastMath.log(FastMath.sqrt(real*real + imaginary*imaginary) + 1) * scale;
        }

        return target.transform(Arrays.asList(amp));
    }

    class ResourceLoader {

        private HashMap<Image, java.awt.Image> cachedImages = new HashMap<>();
        private HashMap<Audio, double[]> cachedAudios = new HashMap<>();
        private ResourceManager resMan;

        public ResourceLoader(ResourceManager resMan) {
            this.resMan = resMan;
        }

        public double[] loadAudio(Audio audio) throws UnsupportedAudioFileException, IOException {
            if (audio == null) {
                cachedAudios.put(audio, null);
            } else if (!cachedAudios.containsKey(audio)) {
                MusicExtractor me = new MusicExtractor(audio.getPath().toString());
                double[] data = me.readFile();
                cachedAudios.put(audio, data);
            }

            return cachedAudios.get(audio);

        }

        public java.awt.Image loadImage(Image image) throws IOException {
            if (image == null) {
                cachedImages.put(image, null);
            }
            if (!cachedImages.containsKey(image)) {
                java.awt.Image result = ImageIO.read(image.getPath().toFile());
                cachedImages.put(image, result);
            }
            return cachedImages.get(image);
        }

    }

}
