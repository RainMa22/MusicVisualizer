package me.rainma22.intermediateimage.Effects.ContainerEffects;

import me.rainma22.intermediateimage.ContainerComponent;
import me.rainma22.intermediateimage.EffectApplier;
import me.rainma22.intermediateimage.Effects.ResourcefulEffect;

public class BackgroundBlurSizeEffect extends ResourcefulEffect<ContainerComponent> {
    private String blurSizeId;

    public BackgroundBlurSizeEffect(ContainerComponent target, String blurSizeId){
        super(target);
        this.blurSizeId = blurSizeId;
    }

    @Override
    public ContainerComponent apply(int currentFrame, EffectApplier applier) {
        return applier.applyBackgroundBlurSizeEffect(currentFrame,this);
    }

    @Override
    public String getName() {
        return "BLURSIZE";
    }

    public String getBlurSizeId() {
        return blurSizeId;
    }

}
