package me.rainma22.musicvisualizer.Image.Resources;

import java.util.HashMap;

public class ResourceManager {
    private HashMap<String, Image> images = new HashMap<>();
    private HashMap<String, Audio> audios = new HashMap<>();

    public ResourceManager(){}

    public Image getImage(String id){
        return images.get(id);
    }

    public String addImage(Image image){
        int idx = images.size();
        String key = Integer.toString(idx);
        setImage(key, image);
        return key;
    }

    public void setImage(String id, Image image){
        images.put(id, image);
    }

    public int numImages(){
        return images.size();
    }

    public Audio findAudio(String id){
        return audios.get(id);
    }

    public String addAudio(Audio audio){
        int idx = audios.size();
        String key = Integer.toString(idx);
        setAudio(key, audio);
        return key;
    }

    public void setAudio(String id, Audio audio){
        audios.put(id, audio);
    }

    public int numAudios(){
        return audios.size();
    }
}
