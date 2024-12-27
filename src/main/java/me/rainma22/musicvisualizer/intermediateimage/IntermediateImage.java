package me.rainma22.musicvisualizer.intermediateimage;

import me.rainma22.musicvisualizer.intermediateimage.Resources.ResourceManager;

/**
 * represents an Image
 */
public class IntermediateImage extends Rectangle {
    private ResourceManager resourceManager;

    public IntermediateImage(int width, int height){
        super(0,0, width, height);
    }

    @Override
    public String getName() {
        return "INTERMEDIATE_IMAGE";
    }

    @Override
    public String selfString() {
        return String.join(" ",
                super.selfString(),
                Integer.toString(resourceManager.numImages()),
                Integer.toString(resourceManager.numAudios()));
    }
}
