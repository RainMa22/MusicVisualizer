package me.rainma22.MusicVisualizer.FrameOutput;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface FrameOutput {
    void writeImage(BufferedImage image, int index) throws IOException;
}
