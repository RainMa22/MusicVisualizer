package me.rainma22.intermediateimage.Resources;

import java.nio.file.Path;

/**
 * Represents an Audio resource
 */

public class Audio extends Resource{

    public Audio(Path path) {
        super(path);
    }
    
    @Override
    public String getName(){
        return "AUDIO";
    }
}