package me.rainma22.musicvisualizer;

import me.rainma22.musicvisualizer.frameoutput.FrameOutput;
import me.rainma22.musicvisualizer.imageprocessor.AwtImageProcessor;
import me.rainma22.musicvisualizer.util.BinaryOperations;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import static me.rainma22.musicvisualizer.util.MusicUtils.createOutput;

public class VisualizationExporter {
    public VisualizationExporter(){}
    public void export(String filePath, int FPS_TARGET, boolean ffmpegEnabled, String outFilePath, AwtImageProcessor processor) throws UnsupportedAudioFileException, FileNotFoundException,IOException {
        MusicExtractor me;
        FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
            me = new MusicExtractor(filePath);
            FrameOutput output = createOutput(filePath, (int) (me.getAudioLengthInSeconds() * FPS_TARGET + .5), ffmpegEnabled, outFilePath);
            double[] samples = me.readFile();

            int samplesPerVideoFrame = (int) (samples.length / (me.getAudioLengthInSeconds() * FPS_TARGET));
            int chunkSize = BinaryOperations.nextPowerOfTwo(samplesPerVideoFrame);
            System.out.printf("required Samples Per Video Frame: %d, calculated ChunkSize: %d\n",
                    samplesPerVideoFrame, chunkSize);
            for (int j = 0; j < samples.length; j += samplesPerVideoFrame) {
                double[] RE = Arrays.copyOfRange(samples, j, j + samplesPerVideoFrame);
                RE = Arrays.copyOf(RE, chunkSize);
                Complex[] transformedData = transformer.transform(RE, TransformType.FORWARD);

                int i = j / samplesPerVideoFrame;
                BufferedImage image = processor.processSample(transformedData);
                output.writeImage(image, i);
                System.out.printf("Image %d out of %d done\n", i + 1, samples.length / samplesPerVideoFrame + 1);
            }
            output.finish();
            me.close();
    }
}
