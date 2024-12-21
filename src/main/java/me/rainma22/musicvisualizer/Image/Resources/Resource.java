package me.rainma22.musicvisualizer.Image.Resources;

import java.nio.file.Path;

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
