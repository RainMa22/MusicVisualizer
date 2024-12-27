package me.rainma22.musicvisualizer.Image;

import me.rainma22.musicvisualizer.Image.Effects.ContainerEffects.BackgroundImage;
import me.rainma22.musicvisualizer.Image.Effects.PolylineEffects.TransformByAudio;
import me.rainma22.musicvisualizer.Image.Resources.ResourceManager;

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
