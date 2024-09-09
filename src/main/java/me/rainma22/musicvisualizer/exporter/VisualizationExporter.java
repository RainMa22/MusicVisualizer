package me.rainma22.musicvisualizer.exporter;

import me.rainma22.musicvisualizer.MusicExtractor;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.rainma22.musicvisualizer.util.MusicUtils.createOutput;

public class VisualizationExporter {
    private List<ExportStatusListener> listeners;

    public VisualizationExporter(){
        listeners = new ArrayList<>(1);
    }

    public void addListener(ExportStatusListener listener){
        if (listeners.contains(listener)) return;
        listeners.add(listener);
    }

    public void removeListener(ExportStatusListener listener){
        listeners.remove(listener);
    }

    public void notifyStatus(int currentFrame, int totalFrame){
        for (ExportStatusListener listener: listeners){
            listener.onStatusUpdate(currentFrame,totalFrame);
        }
    }

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
                notifyStatus(i+1, samples.length / samplesPerVideoFrame + 1);
            }
            output.finish();
            me.close();
    }
}
