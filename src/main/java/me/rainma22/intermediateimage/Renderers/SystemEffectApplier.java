package me.rainma22.intermediateimage.Renderers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import javax.sound.sampled.UnsupportedAudioFileException;
import me.rainma22.intermediateimage.ContainerComponent;
import me.rainma22.intermediateimage.EffectApplier;
import me.rainma22.intermediateimage.Effects.ContainerEffects.BackgroundImage;
import me.rainma22.intermediateimage.Effects.PolylineEffects.TransformByAudio;
import me.rainma22.intermediateimage.PolyLine;
import me.rainma22.intermediateimage.Resources.Audio;
import me.rainma22.intermediateimage.Resources.ResourceManager;
import me.rainma22.musicvisualizer.MusicExtractor;

/**
 * represent a EffectApplier implemented using System's audio library for audio
 * and AWT for image;
 */
public class SystemEffectApplier implements EffectApplier {

    @Override
    public ContainerComponent applyBackgroundImage(int currentFrame, BackgroundImage bi, ResourceManager resMan) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public PolyLine applyTransformByAudio(int currentFrame, TransformByAudio tba, ResourceManager resMan) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    class ResourceLoader {

        private HashMap<String, java.awt.Image> cachedImages = new HashMap<>();
        private HashMap<String, double[]> cachedAudios = new HashMap<>();
        private ResourceManager resMan;

        public ResourceLoader(ResourceManager resMan) {
            this.resMan = resMan;
        }

        public double[] loadAudio(String resId) throws UnsupportedAudioFileException, IOException {
            Audio audio = resMan.getAudio(resId);
            if(audio == null) cachedAudios.put(resId, null);
            if (!cachedAudios.containsKey(resId)) {
                MusicExtractor me = new MusicExtractor(audio.getPath().toString());
                double[] data = me.readFile();
                cachedAudios.put(resId, data);
            }
            
            return cachedAudios.get(resId);

        }

    }

}
