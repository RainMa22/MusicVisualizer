package me.rainma22.musicvisualizer.Image.Resources;

import java.nio.file.Path;

/**
 * Represents an Image Resource
 */
public class Image extends Resource {

    public Image(Path path) {
        super(path);
    }

    @Override
    public String getName() {
        return "Image";
    }
}
