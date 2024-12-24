package me.rainma22.musicvisualizer.Image.Resources;

import java.nio.file.Path;

/**
 * Represents a file resource, to be read by some other class
 */
public abstract class Resource {
    private Path path;
    public Resource(Path path){
        setPath(path);
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
