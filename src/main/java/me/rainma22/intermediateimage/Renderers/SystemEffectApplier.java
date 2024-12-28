package me.rainma22.intermediateimage.Renderers;
import java.util.Hashtable;
import me.rainma22.intermediateimage.ContainerComponent;
import me.rainma22.intermediateimage.EffectApplier;
import me.rainma22.intermediateimage.Effects.ContainerEffects.BackgroundImage;
import me.rainma22.intermediateimage.Effects.PolylineEffects.TransformByAudio;
import me.rainma22.intermediateimage.PolyLine;
import me.rainma22.intermediateimage.Resources.ResourceManager;

/**
 * represent a EffectApplier implemented using System's audio library for audio
 * and AWT for image;
 */
public class SystemEffectApplier implements EffectApplier{

    @Override
    public ContainerComponent applyBackgroundImage(int currentFrame, BackgroundImage bi, ResourceManager resMan) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public PolyLine applyTransformByAudio(int currentFrame, TransformByAudio tba, ResourceManager resMan) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    class ResourceLoader{
        private Hashtable<String, java.awt.Image> cachedImages = new Hashtable<>();        
        private Hashtable<String, double[]> cachedAudios = new Hashtable<>();
        


    }
    
}
