package me.rainma22.MusicVisualizer.FrameOutput;

import org.jcodec.api.awt.AWTSequenceEncoder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * outputs the images as MOV files**/
public class FrameMOVOutput implements FrameOutput {
    private File folder;
    private AWTSequenceEncoder encoder;
    /**
     * creates a new FrameMOVOutput based on the given folderPath and name "output.mkv" and 60fps
     * @param folderPath the path of the Folder;
     * **/

    public FrameMOVOutput(String folderPath) throws IOException {
        this(folderPath, "output.mkv", 60);
    }

    /**
     * creates a new FrameMOVOutput based on the given folderPath and the estimated number of items
     * @param folderPath the path of the Folder;
     * @param fileName the filename for the mkv file
     * @param fps the target fps of the video
     * **/
    public FrameMOVOutput(String folderPath, String fileName, int fps) throws IOException {
        folder = new File(folderPath);
        folder.mkdirs();
        File file = new File(folder,fileName);
        file.createNewFile();
        encoder = AWTSequenceEncoder.createSequenceEncoder(file, fps);

    }

    public void writeImage(BufferedImage image, int index) throws IOException {
        encoder.encodeImage(image);
    }

    public void finish() throws IOException {
        encoder.finish();
    }
}
