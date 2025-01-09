package me.rainma22.intermediateimage;

import me.rainma22.intermediateimage.Effects.ContainerEffects.BackgroundBlurSizeEffect;
import me.rainma22.intermediateimage.Effects.ContainerEffects.BackgroundImage;
import me.rainma22.intermediateimage.Effects.IntermediateImageEffects.BlurSizeEffect;
import me.rainma22.intermediateimage.Effects.PolylineEffects.TransformByAudio;
import me.rainma22.intermediateimage.Resources.Numerical;
import me.rainma22.intermediateimage.Resources.ResourceManager;
import me.rainma22.intermediateimage.Resources.WrongResourceTypeException;

/**
 * represents an effect applier;
 */
public abstract class EffectApplier {

    protected ResourceManager resMan;

    public EffectApplier(ResourceManager resMan) {
        this.resMan = resMan;
    }

    public abstract ContainerComponent applyBackgroundImage(int currentFrame,
                                                            BackgroundImage bi);

    public abstract PolyLine applyTransformByAudio(int currentFrame,
                                                   TransformByAudio tba);

    public IntermediateImage applyBlurSizeEffect(int currentFrame, BlurSizeEffect blurSizeEffect) {
        IntermediateImage result = blurSizeEffect.getTarget().copy();
        try {
            Numerical<Integer> blurSizeNumerical = resMan.getNumerical(Integer.class, blurSizeEffect.getBlurSizeId());
            result.setGaussianBlurSize(blurSizeNumerical.getValue(currentFrame));
        } catch (WrongResourceTypeException e) {
            return result;
        }
        return result;
    }

    public ContainerComponent applyBackgroundBlurSizeEffect(int currentFrame, BackgroundBlurSizeEffect backgroundBlurSizeEffect) {
        ContainerComponent result = backgroundBlurSizeEffect.getTarget().copy();
        try {
            Numerical<Integer> blurSizeNumerical = resMan.getNumerical(Integer.class, backgroundBlurSizeEffect.getBlurSizeId());
            result.setBackgroundGaussianBlurSize(blurSizeNumerical.getValue(currentFrame));
        } catch (WrongResourceTypeException e) {
            e.printStackTrace();
            return result;
        }
        return result;
    }
}
