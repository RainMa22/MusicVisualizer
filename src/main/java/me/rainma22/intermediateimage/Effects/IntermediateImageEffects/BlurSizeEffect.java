package me.rainma22.intermediateimage.Effects.IntermediateImageEffects;

import me.rainma22.intermediateimage.EffectApplier;
import me.rainma22.intermediateimage.Effects.ResourcefulEffect;
import me.rainma22.intermediateimage.IntermediateImage;

public class BlurSizeEffect extends ResourcefulEffect<IntermediateImage> {
    private String blurSizeId;

    public BlurSizeEffect(IntermediateImage target, String blurSizeId){
        super(target);
        this.blurSizeId = blurSizeId;
    }

    @Override
    public IntermediateImage apply(int currentFrame, EffectApplier applier) {
        return applier.applyBlurSizeEffect(currentFrame,this);
    }

    @Override
    public String getName() {
        return "BLURSIZE";
    }

    public String getBlurSizeId() {
        return blurSizeId;
    }

}
