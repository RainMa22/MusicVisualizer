package me.rainma22.MusicVisualizer;

import com.tambapps.fft4j.FastFouriers;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        if (args.length < 2){
            System.out.println("Usage: java -jar MusicVisualizer [CUI|GUI] [filename]");
            return;
        }
        boolean isGUI = args[0].equals("GUI");

        if (!isGUI){
            MusicExtractor me;
            try {
                me = new MusicExtractor(args[1]);
                double[] frames = me.readFile();
                System.out.printf("number of Channel: %d\n", me.getNumChannels());
                int chunkSize= 512;
                for (int j = 0; j < frames.length; j+=chunkSize) {
                    double[] RE = Arrays.copyOfRange(frames,j,j+chunkSize);

                    double[] IM = new double[RE.length];
                    double[] outRE = new double[RE.length];
                    double[] outIM = new double[RE.length];
                    FastFouriers.ITERATIVE_COOLEY_TUKEY.transform(RE, IM,outRE,outIM);

                    for (int i = 0; i < chunkSize; i++) {
                        System.out.printf("sample %d:channel %d: %f, converted to %f\n", (j+i)/2, i%2, RE[i], outRE[i]);
                    }
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