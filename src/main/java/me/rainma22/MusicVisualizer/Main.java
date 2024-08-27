package me.rainma22.MusicVisualizer;

import com.tambapps.fft4j.FastFouriers;
import me.rainma22.MusicVisualizer.ImageProcessor.AwtImageProcessor;

import javax.imageio.ImageIO;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Main {
    private static int chunkSize = 2048;
    private static final float FPS_TARGET = 60;

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Usage: java -jar MusicVisualizer (CUI|GUI) (filename)");
            return;
        }
        boolean isGUI = !args[0].equalsIgnoreCase("CUI");

        AwtImageProcessor processor = new AwtImageProcessor(1920, 1080);
        if (!isGUI) {
            new File("output").mkdir();
            MusicExtractor me;
            try {
                me = new MusicExtractor(args[1]);
                double[] frames = me.readFile();
                System.out.printf("number of Channel: %d\n", me.getNumChannels());
                for (int j = 0; j < frames.length; j += chunkSize) {
                    double[] RE = Arrays.copyOfRange(frames, j, j + chunkSize);

                    double[] IM = new double[RE.length];
                    double[] outRE = new double[RE.length];
                    double[] outIM = new double[RE.length];
                    FastFouriers.ITERATIVE_COOLEY_TUKEY.transform(RE, IM, outRE, outIM);

//                    for (int i = 0; i < CHUNK_SIZE; i++) {
//                        System.out.printf("sample %d:channel %d: %f, converted to %f+%fi\n", (j + i) / 2, i % 2, RE[i],
//                                outRE[i], outIM[i]);
//                    }
                    int i = j/ chunkSize;
                    BufferedImage image = processor.processSample(outRE, outIM);
                    String fileName = String.format("output/%04d.png", i);
                    ImageIO.write(image, "png", new File(fileName));
                    System.out.printf("Written Image %d out of %d\n", i, frames.length/ chunkSize + 1);
                }
                me.close();
            } catch (UnsupportedAudioFileException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}