package me.rainma22.intermediateimage.Effects.PolylineEffects;

import me.rainma22.intermediateimage.EffectApplier;
import me.rainma22.intermediateimage.Effects.ResourcefulEffect;
import me.rainma22.intermediateimage.PolyLine;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;

/**
 * An ReourcefulEffect that transforms a given PolyLine by the magnitude of the
 * audio resource using Fourier Transform
 */
public class TransformByAudio extends ResourcefulEffect<PolyLine> {

    private FastFourierTransformer transformer
            = new FastFourierTransformer(DftNormalization.STANDARD);
    private String audioId = null;
    private String scaleId = null;
    private String strokeSizeId = null;

    /**
     * Constructor
     *
     * @param target the PolyLine to apply this effect to.
     * @param audioId the resource id of the audio
     * @param scaleId the resource id of the numerical value for amplitude scale
     * @param strokeSizeId the resource id of the numerical value for stroke size
     */
    public TransformByAudio(PolyLine target, String audioId, String scaleId, String strokeSizeId) {
        super(target);
        this.audioId = audioId;
        this.scaleId = scaleId;
        this.strokeSizeId = strokeSizeId;
    }

    public String getAudioId() {
        return audioId;
    }

    public String getScaleId() {
        return scaleId;
    }

    public String getStrokeSizeId() {
        return strokeSizeId;
    }

    @Override
    public String getName() {
        return "TRANSFORM_BY_AUDIO";
    }

    public FastFourierTransformer getTransformer() {
        return transformer;
    }

    @Override
    public PolyLine apply(int currentFrame, EffectApplier applier) {
        return applier.applyTransformByAudio(currentFrame, this);
    }
}
