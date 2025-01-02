package me.rainma22.intermediateimage.Effects.ContainerEffects;

import me.rainma22.intermediateimage.ContainerComponent;
import me.rainma22.intermediateimage.EffectApplier;
import me.rainma22.intermediateimage.Effects.ResourcefulEffect;

/**
 * Represents a background image effect
 */
public class BackgroundImage extends ResourcefulEffect<ContainerComponent> {

    private String imageId = null;

    public BackgroundImage(ContainerComponent target, String imageId) {
        super(target);
        this.imageId = imageId;
    }

    public String getImageId() {
        return imageId;
    }

    @Override
    public String getName() {
        return "BACKGROUND_IMG";
    }

    @Override
    public ContainerComponent apply(int currentFrame, EffectApplier applier) {
        return applier.applyBackgroundImage(currentFrame, this);
    }

}
