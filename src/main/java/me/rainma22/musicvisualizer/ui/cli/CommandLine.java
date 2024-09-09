package me.rainma22.musicvisualizer.ui.cli;

import me.rainma22.musicvisualizer.exporter.ExportStatusListener;
import me.rainma22.musicvisualizer.exporter.VisualizationExporter;
import me.rainma22.musicvisualizer.imageprocessor.AwtImageProcessor;
import me.rainma22.musicvisualizer.settings.SettingsManager;
import org.apache.commons.math3.util.FastMath;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class CommandLine implements ExportStatusListener {
    private final SettingsManager settings;
    private final String filePath;
    private final boolean ffmpegEnabled;
    private final String outFilePath;

    public CommandLine(String filePath, boolean ffmpegEnabled, String outFilePath) {
        settings = SettingsManager.getSettingsManager();
        this.filePath = filePath;
        this.ffmpegEnabled = ffmpegEnabled;
        this.outFilePath = outFilePath;
    }

    public void start() {
        double FPS_TARGET = settings.getDouble("fps", 60);
        int width = settings.getInt("width", 1920);
        int height = settings.getInt("height", 1080);
        double rotationPerTheta = settings.getDouble("rotation_per_theta", 0);//Math.PI/60
        double amplitudeThreshold = settings.getDouble("amplitude_threshold", 0);
        int chunkSize;


        BufferedImage foregroundImage = settings.getImage("foreground_img", null);
        BufferedImage backgroundImage = settings.getImage("background_img", null);
        Color lineColor = settings.getColor("line_color_hex", Color.BLUE);
        int blurSize = Math.max(0, settings.getInt("blur_size", 50));
        if (blurSize % 2 == 0) blurSize++;

        AwtImageProcessor processor = new AwtImageProcessor(width, height, amplitudeThreshold,
                foregroundImage, backgroundImage, rotationPerTheta);

        processor.setLineColor(lineColor);
        processor.setBlurSize(blurSize);

        VisualizationExporter exporter = new VisualizationExporter();
        exporter.addListener(this);

        try {
            exporter.export(filePath, FastMath.round((float) FPS_TARGET), ffmpegEnabled, outFilePath, processor);
        } catch (UnsupportedAudioFileException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void onStatusUpdate(int currentFrame, int totalFrame) {
        System.out.printf("Image %d out of %d done\n", currentFrame, totalFrame);
    }
}
