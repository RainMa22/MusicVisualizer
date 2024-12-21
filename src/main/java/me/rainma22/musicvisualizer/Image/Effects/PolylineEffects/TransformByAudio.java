package me.rainma22.musicvisualizer.Image.Effects.PolylineEffects;

import me.rainma22.musicvisualizer.Image.Effects.AudioEffect;
import me.rainma22.musicvisualizer.Image.PolyLine;
import me.rainma22.musicvisualizer.Image.Resources.Audio;
import me.rainma22.musicvisualizer.MusicExtractor;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.apache.commons.math3.util.FastMath;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.Arrays;

public class TransformByAudio extends AudioEffect<PolyLine> {
    double data[] = null;
    FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);

    public TransformByAudio(PolyLine target, Audio audio) {
        super(target, audio);
        MusicExtractor extractor = null;
        try {
            extractor = new MusicExtractor(audio.getPath().toString());
            data = extractor.readFile();
        } catch (UnsupportedAudioFileException e) {
            //TODO: ignored
        } catch (IOException e) {
            //TODO: ignored
        }
    }

    public PolyLine apply(int index){
        if(data == null) return target;

        double[] subData = Arrays.copyOfRange(data, index*target.size(),(index+1)*target.size());
        Complex[] transformedData = transformer.transform(subData, TransformType.FORWARD);

        Float[] amp = new Float[transformedData.length];
        for (int i = 0; i < transformedData.length; i++) {
            double real = transformedData[i].getReal();
            double imaginary = transformedData[i].getImaginary();
            amp[i] = (float) FastMath.log(FastMath.sqrt(real*real + imaginary*imaginary) + 1);
        }

        return target.transform(Arrays.asList(amp));

    }
}
