package me.rainma22.musicvisualizer.frameoutput;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface FrameOutput {
    void writeImage(BufferedImage image, int index) throws IOException;
    default void finish() throws IOException{};
}
