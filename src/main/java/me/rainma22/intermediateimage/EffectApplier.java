package me.rainma22.intermediateimage;

import me.rainma22.intermediateimage.Effects.ContainerEffects.BackgroundImage;
import me.rainma22.intermediateimage.Effects.PolylineEffects.TransformByAudio;
import me.rainma22.intermediateimage.Resources.ResourceManager;

/**
 * represents an effect applier;
 */
public abstract class EffectApplier {
    
    protected ResourceManager resMan;
    
    public EffectApplier(ResourceManager resMan){
        this.resMan = resMan;
    }

    public abstract ContainerComponent applyBackgroundImage(int currentFrame,
            BackgroundImage bi);

    public abstract PolyLine applyTransformByAudio(int currentFrame,
            TransformByAudio tba);

}
