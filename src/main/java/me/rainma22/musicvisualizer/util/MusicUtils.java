package me.rainma22.musicvisualizer.util;

import me.rainma22.musicvisualizer.frameoutput.FrameFFMPEGOutput;
import me.rainma22.musicvisualizer.frameoutput.FrameMOVOutput;
import me.rainma22.musicvisualizer.frameoutput.FrameOutput;
import me.rainma22.musicvisualizer.frameoutput.FramePNGOutput;
import me.rainma22.musicvisualizer.SettingsManager;

import java.io.IOException;

public class MusicUtils {

    public static FrameOutput createOutput(String pathToAudio, int numFrames, boolean ffmpegEnabled) throws IOException {
        SettingsManager settings = SettingsManager.getSettingsManager();
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
}
