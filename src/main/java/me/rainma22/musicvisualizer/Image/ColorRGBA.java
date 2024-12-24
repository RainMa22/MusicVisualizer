
package me.rainma22.musicvisualizer.Image;

public class ColorRGBA{
    
    public static final ColorRGBA TRANSPARENT = new ColorRGBA(0);
    private byte red, green, blue, alpha;
    
    public ColorRGBA(int rgba){
        red = (byte) ((rgba & 0xff000000)>> 24);
        green = (byte) (rgba & 0x00ff0000 >> 16);        
        blue = (byte) (rgba & 0x0000ff00 >> 8);
        alpha = (byte) (rgba & 0x000000ff);
    }

    public byte getRed() {
        return red;
    }

    public void setRed(byte red) {
        this.red = red;
    }

    public byte getGreen() {
        return green;
    }

    public void setGreen(byte green) {
        this.green = green;
    }

    public byte getBlue() {
        return blue;
    }

    public void setBlue(byte blue) {
        this.blue = blue;
    }

    public byte getAlpha() {
        return alpha;
    }

    public void setAlpha(byte alpha) {
        this.alpha = alpha;
    }
    
    public int intValue(){
        return ((red & 0xFF) << 24) | 
                ((green & 0xFF) << 16) | 
                ((blue & 0xFF) << 8) | 
                (alpha & 0xFF);
    }
    
    @Override
    public String toString(){
        return "0x".concat(Integer.toHexString(intValue()));
    }
}
