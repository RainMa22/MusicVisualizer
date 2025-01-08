package me.rainma22.intermediateimage.Resources;

import java.nio.file.Path;

/**
 * Represents a file resource, to be read by some other class
 */
public abstract class FileResource extends BaseResource {
    private Path path;
    public FileResource(Path path){
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
        return String.join(" ", super.toString(), relPath);
    }

}
