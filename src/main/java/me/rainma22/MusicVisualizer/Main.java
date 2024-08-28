package me.rainma22.MusicVisualizer;

import com.tambapps.fft4j.FastFouriers;
import me.rainma22.MusicVisualizer.FrameOutput.FrameMOVOutput;
import me.rainma22.MusicVisualizer.FrameOutput.FrameOutput;
import me.rainma22.MusicVisualizer.ImageProcessor.AwtImageProcessor;
import me.rainma22.MusicVisualizer.Utils.BinaryOperations;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

public class Main {
    private static int chunkSize;
    private static final float FPS_TARGET = 60;

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Usage: java -jar MusicVisualizer (CUI|GUI) (filename)");
            return;
        }
        boolean isGUI = !args[0].equalsIgnoreCase("CUI");

        AwtImageProcessor processor = new AwtImageProcessor(1920, 1080);
        if (!isGUI) {
//            new File("output").mkdir();
//            FrameOutput output = new FramePNGOutput("output");
            FrameOutput output;

            MusicExtractor me;
            try {
                output = new FrameMOVOutput("output","out.mov", (int)FPS_TARGET);
                me = new MusicExtractor(args[1]);


                double[] samples = me.readFile();

                int samplesPerVideoFrame = (int) (samples.length/(me.getAudioLengthInSeconds()*FPS_TARGET));
                chunkSize = BinaryOperations.nextPowerOfTwo(samplesPerVideoFrame);
                System.out.printf("required Samples Per Video Frame: %d, calculated ChunkSize: %d\n",
                        samplesPerVideoFrame,chunkSize);
                for (int j = 0; j < samples.length; j += samplesPerVideoFrame) {
                    double[] RE = Arrays.copyOfRange(samples, j, j + samplesPerVideoFrame);
                    RE = Arrays.copyOf(RE, chunkSize);
                    double[] IM = new double[chunkSize];
                    double[] outRE = new double[chunkSize];
                    double[] outIM = new double[chunkSize];
                    FastFouriers.ITERATIVE_COOLEY_TUKEY.transform(RE, IM, outRE, outIM);

                    int i = j/ samplesPerVideoFrame;
                    BufferedImage image = processor.processSample(outRE, outIM);
                    output.writeImage(image, i);
                    System.out.printf("Image %d out of %d done\n", i+1, samples.length/samplesPerVideoFrame+1);
                }
                output.finish();
                me.close();
            } catch (UnsupportedAudioFileException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}