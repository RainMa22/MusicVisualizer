package me.rainma22.intermediateimage.Resources;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A data-class containing all the resources, keyed by their resourceIds
 */
public class ResourceManager {

    private static final String IMAGE_PREFIX = "IMAGE_";
    private static final String AUDIO_PREFIX = "AUDIO_";
    private static final String NUMERICAL_PREFIX = "AUDIO_";

    private static final HashMap<Class<?>, String> prefixMap = new HashMap<>();

    static {
        prefixMap.put(Image.class, "IMAGE_");
        prefixMap.put(Audio.class, "Audio_");
        prefixMap.put(Numerical.class, "NUMERICAL_");
        prefixMap.put(BaseResource.class, "UNKNOWN_RESOURCE_");
    }

//    private Map<String, Image> images = new HashMap<>();
//    private Map<String, Audio> audios = new HashMap<>();

    private Map<String, BaseResource> resourceMap = new HashMap<>();

    public ResourceManager() {
    }

    private BaseResource getResource(String id) {
        return resourceMap.get(id);
    }

    public void set(String id, BaseResource resource) {
        resourceMap.put(id, resource);
    }

    public <T extends BaseResource> String add(Class<T> aClass, BaseResource toAdd) {
        String key = nextUnusedDefaultKey(
                prefixMap.getOrDefault(aClass, prefixMap.get(BaseResource.class)),
                resourceMap.keySet());
        set(key, toAdd);
        return key;

    }

    public <T extends BaseResource> int numResourceOfType(Class<T> aClass) {
        int num = 0;
        for (String key : resourceMap.keySet()) {
            if (aClass.isInstance(resourceMap.get(key))) num++;
        }
        return num;
    }

    public <T extends BaseResource> T get(Class<T> aClass, String id)
            throws WrongResourceTypeException {
        try {
            return aClass.cast(getResource(id));
        } catch (ClassCastException cce) {
            throw new WrongResourceTypeException(cce);
        }
    }

    public <T extends Number> Numerical<T> getNumerical(Class<T> numClass, String id)
            throws WrongResourceTypeException {
        Numerical<? extends Number> result = (Numerical<? extends Number>) get(Numerical.class, id);
        if (!numClass.isInstance(result.getValue())) throw new WrongResourceTypeException();

        return (Numerical<T>) result;
    }

    /**
     * Adds the image to the Audio data set
     *
     * @param audio the audio to add
     * @return the resource id String that corresponds to the audio
     */
    public String addAudio(Audio audio) {
        return add(Audio.class, audio);
    }

    public Map<String, BaseResource> getResourceMap() {
        return resourceMap;
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
}
