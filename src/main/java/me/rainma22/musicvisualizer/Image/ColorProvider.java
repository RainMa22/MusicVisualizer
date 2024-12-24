
package me.rainma22.musicvisualizer.Image;

import me.rainma22.musicvisualizer.Image.Resources.Image;
import me.rainma22.musicvisualizer.Image.Resources.ResourceManager;

public class ColorProvider {
    private ColorRGBA color;
    private String imageId;
    
    private ColorProvider(){
        color = null;
        imageId = null;
    };
    
    public static ColorProvider ofColor(ColorRGBA color){
        ColorProvider result = new ColorProvider();
        result.color = color;
        return result;
    }
    
    public static ColorProvider ofImage(String imageId){
        ColorProvider result = new ColorProvider();
        result.imageId = imageId;
        return result;
    }
    
    public ColorRGBA getColor() throws UnsupportedOperationException{
        if(color == null) throw new UnsupportedOperationException();
        return color;
    }
    
    public Image getImage(ResourceManager ResMan) 
            throws UnsupportedOperationException{
        if(imageId == null) throw new UnsupportedOperationException();
        return ResMan.getImage(imageId);
    }
    
    public boolean isPureColor(){
        return color != null;
    }
    
    public boolean isImage(){
        return imageId != null;
    }
    
    @Override
    public String toString(){
        if(isPureColor()){
            return color.toString();
        } else {
            return imageId;
        }
    }
}


