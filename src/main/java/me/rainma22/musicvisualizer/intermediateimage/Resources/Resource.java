package me.rainma22.musicvisualizer.intermediateimage.Resources;

import java.io.File;
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
    
    public abstract String getName();
    @Override
    public String toString(){
        String relPath = path.toUri().relativize(Path.of("./").toUri()).getPath();
        return String.join(" ", getName(), relPath);
    }

}
