package me.rainma22.MusicVisualizer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

/**
 * A class that extracts wav file into pcm data frame
 **/
public class MusicExtractor {

    AudioInputStream audioInputStream;
    String filePath;

    /**
     * creates a new MusicExtractor with the given file path, Throws UnsupportedAudioFileException if
     * audio file is unsupported(duh), and IOException if encountering some unknown IO issues;
     **/
    public MusicExtractor(String filePath) throws UnsupportedAudioFileException, IOException {
        this.filePath = filePath;
        audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
    }
    /**
     * closes the AudioInputStream
     * **/
    public void close(){
        try {
            audioInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * normalize the sample based on the number of bitsPerSample of the audioInputStream
     **/
    private double normalize(double sample, int bitsPerSample) {
        return sample / (1 << (bitsPerSample));
    }

    private double normalize(double sample) {
        return normalize(sample, audioInputStream.getFormat().getSampleSizeInBits());
    }

    /**
     * returns the number of channels of the file
     **/
    public int getNumChannels() {
        return audioInputStream.getFormat().getChannels();
    }

    /**
     * read file and extract the sample data of the file as an array of doubles,
     * if file is dual channel, the file array will store it in the following order:
     * [sample0:channel0, sample0:channel1, sample1:channel0, sample1:channel1, ...]
     **/
    public double[] readFile() throws IOException {
        byte[] data = audioInputStream.readAllBytes();
        int bitsPerSample = audioInputStream.getFormat().getSampleSizeInBits();
        int bytesPerSample = bitsPerSample/8;
        double[] samples = new double[data.length*8/bitsPerSample];
        double sample;
        long temp;

        for (int index = 0; index < samples.length; index++) {
            sample = 0f;
            temp = 0;

            int i = index*bytesPerSample;
            for (int goal = i + bytesPerSample; i < goal; i ++) {
                if (audioInputStream.getFormat().isBigEndian()) {
                    temp = temp << 8 + data[i] & 0xffL;
                } else {
                    int j = goal - i;
                    j = bytesPerSample - j;
                    temp += (data[i] & 0xffL) << (8*j);
                }
            }

            switch (audioInputStream.getFormat().getEncoding().toString()) {
                case "PCM_SIGNED":
                    int bitsToExtend = Long.SIZE - bitsPerSample;
                    sample = (temp << bitsToExtend) >> bitsToExtend;
                    break;
                case "PCM_UNSIGNED":
                    sample = temp - (1L << bitsPerSample);
                    break;
                case "PCM_FLOAT":
                    sample = Double.longBitsToDouble(temp);
                    break;
                case "ALAW":
                    temp ^= 0x55L;
                    if ((temp & 0x80L) != 0) {
                        temp ^= 0x80L;
                        temp = -temp;
                    }
                    sample = normalize((double) temp);
                    break;
                case "ULAW":
                    temp ^= 0xffL;
                    if ((temp & 0x80L) != 0) {
                        temp ^= 0x80L;
                        temp = -temp;
                    }
                    sample = normalize((double) temp);
                    break;
            }
            samples[index] = sample;
        }
        return samples;

    }
}
