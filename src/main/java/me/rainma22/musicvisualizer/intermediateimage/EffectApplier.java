package me.rainma22.musicvisualizer.intermediateimage;

import me.rainma22.musicvisualizer.intermediateimage.Effects.ContainerEffects.BackgroundImage;
import me.rainma22.musicvisualizer.intermediateimage.Effects.PolylineEffects.TransformByAudio;
import me.rainma22.musicvisualizer.intermediateimage.Resources.ResourceManager;

/**
 * represents an effect applier;
 */
public interface EffectApplier {

    public ContainerComponent applyBackgroundImage(int currentFrame,
            BackgroundImage bi,
            ResourceManager resMan);

    public PolyLine applyTransformByAudio(int currentFrame,
            TransformByAudio tba,
            ResourceManager resMan);

}
