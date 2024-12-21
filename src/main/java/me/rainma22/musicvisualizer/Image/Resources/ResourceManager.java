package me.rainma22.musicvisualizer.Image.Resources;

import java.util.ArrayList;

public class ResourceManager {
    private ArrayList<Image> images = new ArrayList<>();
    private ArrayList<Audio> audios = new ArrayList<>();

    public ResourceManager(){}

    public Image getImage(int id){
        return images.get(id);
    }

    public int addImage(Image image){
        int idx = images.size();
        images.add(image);
        return idx;
    }

    public void setImage(int id, Image image){
        images.set(id, image);
    }

    public int numImages(){
        return images.size();
    }

    public Audio findAudio(int id){
        return audios.get(id);
    }

    public int addAudio(Audio audio){
        int idx = audios.size();
        audios.add(audio);
        return idx;
    }

    public void setAudio(int idx, Audio audio){
        audios.set(idx, audio);
    }

    public int numAudios(){
        return audios.size();
    }
}
