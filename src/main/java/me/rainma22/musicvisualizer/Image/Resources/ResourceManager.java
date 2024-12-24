package me.rainma22.musicvisualizer.Image.Resources;

import java.util.HashMap;

/**
 * A data-class containing all the resources, keyed by their resourceIds
 *
 */
public class ResourceManager {

    private HashMap<String, Image> images = new HashMap<>();
    private HashMap<String, Audio> audios = new HashMap<>();

    public ResourceManager() {
    }

    public Image getImage(String id) {
        return images.get(id);
    }

    // TODO: add renaming function to set resources to a different key
    // TODO: getters for the resources, make sure the HashMaps is a different copy
    /**
     * Adds the image to the Image data set
     *
     * TODO: add Collision checking
     *
     * @param image the image to add
     * @return the resource id String that corresponds to the image;
     */
    public String addImage(Image image) {
        int idx = images.size();
        String key = Integer.toString(idx);
        setImage(key, image);
        return key;
    }

    public void setImage(String id, Image image) {
        images.put(id, image);
    }

    /**
     * @return the amount of images stored in the resource manager
     */
    public int numImages() {
        return images.size();
    }

    public Audio findAudio(String id) {
        return audios.get(id);
    }

    /**
     * Adds the image to the Audio data set
     *
     * TODO: add Collision checking
     *
     * @param audio the audio to add
     * @return the resource id String that corresponds to the audio
     */
    public String addAudio(Audio audio) {
        int idx = audios.size();
        String key = Integer.toString(idx);
        setAudio(key, audio);
        return key;
    }

    public void setAudio(String id, Audio audio) {
        audios.put(id, audio);
    }

    /**
     *
     * @return the amount of audio in the audio data set
     */
    public int numAudios() {
        return audios.size();
    }
}
