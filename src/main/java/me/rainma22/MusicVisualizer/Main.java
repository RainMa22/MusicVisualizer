package me.rainma22.MusicVisualizer;

import com.tambapps.fft4j.FastFouriers;
import me.rainma22.MusicVisualizer.FrameOutput.FrameFFMPEGOutput;
import me.rainma22.MusicVisualizer.FrameOutput.FrameMOVOutput;
import me.rainma22.MusicVisualizer.FrameOutput.FrameOutput;
import me.rainma22.MusicVisualizer.FrameOutput.FramePNGOutput;
import me.rainma22.MusicVisualizer.ImageProcessor.AwtImageProcessor;
import me.rainma22.MusicVisualizer.Utils.BinaryOperations;
import me.rainma22.MusicVisualizer.Utils.ProcessUtils;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

public class Main {
    private static int chunkSize;
    private static boolean ffmpegEnabled = false;
    private static SettingsManager settings = SettingsManager.getSettingsManager();


    static FrameOutput createOutput(String pathToAudio,int numFrames) throws IOException {
        String format = settings.get("output_format");
        String outPath = settings.get("output_path");
        String outFileName = settings.get("out_file_name");
        outFileName = String.join(".", outFileName, format);
        int width = settings.getInt("width", 1920);
        int height = settings.getInt("height", 1080);
        String ffmpegPath = settings.get("path_to_ffmpeg");
        double fps = settings.getDouble("fps", 60);
        switch (format) {
            case "PNG":
                System.out.println("Using PNG output");
                return new FramePNGOutput(outPath, numFrames);
            case "MOV":
            default:
                if (ffmpegEnabled) {
                    System.out.println("Using FFmpeg to output video with sound");
                    return new FrameFFMPEGOutput(outPath, outFileName, pathToAudio, ffmpegPath, width, height, fps);
                } else {
                    System.out.println("Using JCodec to output soundless MOV");
                    return new FrameMOVOutput(outPath, outFileName, (int) fps);
                }
        }
    }

    public static void main(String[] args) {
        //Check if enough args are used
        if (args.length < 2) {
            System.out.println("Usage: java -jar MusicVisualizer (CLI|GUI) (filename) " +
                    settings.getSettingsString());
            return;
        }

        boolean isGUI = !args[0].equalsIgnoreCase("CLI");
        if (isGUI) {
            System.err.println("GUI not Implemented, exiting");
            return;
        }

        // Parse Settings
        for (int i = 2; i < args.length; i++) {
            String[] keyValPair = args[i].split("=");
            if (keyValPair.length < 2) {
                System.err.printf("Argument \"%s\" does not have a equal sign, ignoring! \n", args[i]);
            } else if (keyValPair.length > 2) {
                System.err.printf("Argument \"%s\" has too many equal signs, using \"%s=%s\"!\n", args[i],
                        keyValPair[0], keyValPair[1]);
            }

            if (!settings.containsKey(keyValPair[0])) {
                System.err.printf("Unknown key %s! Ignoring\n", keyValPair[0]);
            } else {
                settings.put(keyValPair[0], keyValPair[1]);
            }
        }

        double FPS_TARGET = settings.getDouble("fps", 60);
        String ffmpegPath = settings.get("path_to_ffmpeg");
        int width = settings.getInt("width", 1920);
        int height = settings.getInt("height", 1080);
        BufferedImage foregroundImage = settings.getImage("foreground_img", null);
        double rotationPerTheta = settings.getDouble("rotation_per_theta", Math.PI/60);
        double amplitudeThreshold = settings.getDouble("amplitude_threshold", 0);

        if (!(ffmpegEnabled = ProcessUtils.isProgramRunnable(ffmpegPath))) {
            System.err.println("FFmpeg not usable! Using JCodec instead(Very Slow and no Audio in video output)!");
        }

        Color lineColor = settings.getColor("line_color_hex", Color.black);
        AwtImageProcessor processor = new AwtImageProcessor(width, height, amplitudeThreshold,
                foregroundImage,rotationPerTheta);
        processor.setLineColor(lineColor);

        FrameOutput output;
        MusicExtractor me;
        try {
            me = new MusicExtractor(args[1]);
            output = createOutput(args[1],(int) (me.getAudioLengthInSeconds() * FPS_TARGET + .5));

            double[] samples = me.readFile();

            int samplesPerVideoFrame = (int) (samples.length / (me.getAudioLengthInSeconds() * FPS_TARGET));
            chunkSize = BinaryOperations.nextPowerOfTwo(samplesPerVideoFrame);
            System.out.printf("required Samples Per Video Frame: %d, calculated ChunkSize: %d\n",
                    samplesPerVideoFrame, chunkSize);
            for (int j = 0; j < samples.length; j += samplesPerVideoFrame) {
                double[] RE = Arrays.copyOfRange(samples, j, j + samplesPerVideoFrame);
                RE = Arrays.copyOf(RE, chunkSize);
                double[] IM = new double[chunkSize];
                double[] outRE = new double[chunkSize];
                double[] outIM = new double[chunkSize];
                FastFouriers.ITERATIVE_COOLEY_TUKEY.transform(RE, IM, outRE, outIM);

                int i = j / samplesPerVideoFrame;
                BufferedImage image = processor.processSample(outRE, outIM);
                output.writeImage(image, i);
                System.out.printf("Image %d out of %d done\n", i + 1, samples.length / samplesPerVideoFrame + 1);
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