package me.rainma22.MusicVisualizer.FrameOutput;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * outputs the images as PNG files**/
public class FramePNGOutput implements FrameOutput {
    private File folder;
    private String formatString;

    /**
     * creates a new FramePNGOutput based on the given folderPath and allowing naming up to 100,000 number of images
     * @param folderPath the path of the Folder;
     * **/

    public FramePNGOutput(String folderPath){
        this(folderPath, (int) Math.pow(10,5));
    }

    /**
     * creates a new FramePNGOutput based on the given folderPath and the estimated number of items
     * @param folderPath the path of the Folder;
     * @param estimatedNumItems the estimated number of Items
     * **/
    public FramePNGOutput(String folderPath, int estimatedNumItems){
        formatString = "%0"+Math.ceil(Math.log10(estimatedNumItems))+".png";
        folder = new File(folderPath);
        folder.mkdirs();
    }

    public void writeImage(BufferedImage image, int index) throws IOException {
        ImageIO.write(image, "PNG", new File(String.format(formatString, index)));
    }
}
