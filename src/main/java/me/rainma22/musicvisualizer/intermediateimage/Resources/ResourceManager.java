package me.rainma22.musicvisualizer.intermediateimage.Resources;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A data-class containing all the resources, keyed by their resourceIds
 *
 */
public class ResourceManager {

    private static final String IMAGE_PREFIX = "IMAGE_";
    private static final String AUDIO_PREFIX = "AUDIO_";

    private Map<String, Image> images = new HashMap<>();
    private Map<String, Audio> audios = new HashMap<>();

    public ResourceManager() {
    }

    public Image getImage(String id) {
        return images.get(id);
    }

    public void setImage(String id, Image image) {
        images.put(id, image);
    }

    public Map<String, Image> getImages() {
        return Map.copyOf(images);
    }

    public Map<String, Audio> getAudios() {
        return Map.copyOf(audios);
    }

    private String nextUnusedDefaultKey(String prefix, Set<String> keys) {
        String key = null;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            key = prefix.concat(Integer.toString(i));

            if (!keys.contains(key)) {
                return key;
            }
        }
        return key;
    }

    /**
     * Adds the image to the Image data set
     *
     * @param image the image to add
     * @return the resource id String that corresponds to the image;
     */
    public String addImage(Image image) {
        String key = nextUnusedDefaultKey(IMAGE_PREFIX,
                images.keySet());
        setImage(key, image);
        return key;
    }

    /**
     * @return the amount of images stored in the resource manager
     */
    public int numImages() {
        return images.size();
    }

    public Audio getAudio(String id) {
        return audios.get(id);
    }

    public void setAudio(String id, Audio audio) {
        audios.put(id, audio);
    }

    /**
     * Adds the image to the Audio data set
     *
     * @param audio the audio to add
     * @return the resource id String that corresponds to the audio
     */
    public String addAudio(Audio audio) {
        String key = nextUnusedDefaultKey(AUDIO_PREFIX,
                audios.keySet());
        setAudio(key, audio);
        return key;
    }

    /**
     *
     * @return the amount of audio in the audio data set
     */
    public int numAudios() {
        return audios.size();
    }
}
