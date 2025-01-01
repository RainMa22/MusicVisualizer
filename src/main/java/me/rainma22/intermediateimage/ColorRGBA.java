
package me.rainma22.intermediateimage;
/**
 * A Color represents a RGBA value
 */
public class ColorRGBA{
    public static final ColorRGBA BLACK = new ColorRGBA(0x000000ff);
    public static final ColorRGBA TRANSPARENT = new ColorRGBA(0x00000000);
    private byte red, green, blue, alpha;
    
    public ColorRGBA(int rgba){
        red = (byte) ((rgba & 0xff000000)>> 24);
        green = (byte) ((rgba & 0x00ff0000) >> 16);
        blue = (byte) ((rgba & 0x0000ff00) >> 8);
        alpha = (byte) (rgba & 0x000000ff);
    }

    public int getRed() {
        return red & 0xff;
    }

//    public void setRed(byte red) {
//        this.red = red;
//    }

    public int getGreen() {
        return green & 0xff;
    }

//    public void setGreen(byte green) {
//        this.green = green;
//    }

    public int getBlue() {
        return blue & 0xff;
    }

//    public void setBlue(byte blue) {
//        this.blue = blue;
//    }

    public int getAlpha() {
        return alpha & 0xff;
    }

//    public void setAlpha(byte alpha) {
//        this.alpha = alpha;
//    }
    
    public int intValue(){
        return ((red & 0xFF) << 24) | 
                ((green & 0xFF) << 16) | 
                ((blue & 0xFF) << 8) | 
                (alpha & 0xFF);
    }
    
    @Override
    public String toString(){
        return "COLOR ".concat(Integer.toHexString(intValue()));
    }
}
