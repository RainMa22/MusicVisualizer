package me.rainma22.musicvisualizer.intermediateimage.Effects.PolylineEffects;

import me.rainma22.musicvisualizer.intermediateimage.Effects.ResourcefulEffect;
import me.rainma22.musicvisualizer.intermediateimage.PolyLine;
import me.rainma22.musicvisualizer.intermediateimage.Resources.Audio;
import me.rainma22.musicvisualizer.MusicExtractor;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.apache.commons.math3.util.FastMath;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import me.rainma22.musicvisualizer.intermediateimage.EffectApplier;
import me.rainma22.musicvisualizer.intermediateimage.Resources.ResourceManager;
import me.rainma22.musicvisualizer.util.BinaryOperations;

/**
 * An ReourcefulEffect that transforms a given PolyLine by 
 * the magnitude of the audio resource using Fourier Transform
 */
public class TransformByAudio extends ResourcefulEffect<PolyLine> {
    double data[] = null;
    FastFourierTransformer transformer = 
            new FastFourierTransformer(DftNormalization.STANDARD);

    /**
     * Constructor
     * 
     * @param target the PolyLine to apply this effect to.
     * @param resourceIds the resourceIds needed by this effect,
       where the first item is the audio resource needed,
       if the resourceIds is empty, apply() will simply 
       return a copy of the target.
     */
    public TransformByAudio(PolyLine target, List<String> resourceIds) {
        super(target, resourceIds);
    }
    
    @Override
    public String getName(){
        return "TRANSFORM_BY_AUDIO";
    }

//    /**
//     * @param index
//     * @param resMan
//     * @return the PolyLine with the audio transformation applied,
//               or the copy of the PolyLine if resourceIds is empty,
//               audio cannot be found, or file cannot be read
//     */
//    @Override
//    public PolyLine apply(int index, ResourceManager resMan){
//        if(resourceIds.isEmpty()) return target.copy();
//        Audio audio = resMan.getAudio(resourceIds.getFirst());
//        if(audio == null) return target.copy();
//        MusicExtractor extractor = null;
//        try {
//            extractor = new MusicExtractor(audio.getPath().toString());
//            data = extractor.readFile();
//        } catch (UnsupportedAudioFileException e) {
//            //TODO: ignored
//        } catch (IOException e) {
//            //TODO: ignored
//        }
//        if(data == null) return target.copy();
//
//        double[] subData = Arrays.copyOfRange(data, index*target.size(),(index+1)*target.size());
//        subData = Arrays.copyOf(subData, 
//                BinaryOperations.nextPowerOfTwo(index));
//        Complex[] transformedData = transformer.transform(subData, TransformType.FORWARD);
//
//        Float[] amp = new Float[target.size()];
//        for (int i = 0; i < amp.length; i++) {
//            double real = transformedData[i].getReal();
//            double imaginary = transformedData[i].getImaginary();
//            amp[i] = (float) FastMath.log(FastMath.sqrt(real*real + imaginary*imaginary) + 1);
//        }
//
//        return target.transform(Arrays.asList(amp));
//    }

    @Override
    public PolyLine apply(int currentFrame, EffectApplier applier, ResourceManager resMan) {
        return applier.applyTransformByAudio(currentFrame, this, resMan);
    }
}
