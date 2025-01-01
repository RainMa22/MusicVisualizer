package me.rainma22.intermediateimage;

import me.rainma22.intermediateimage.Resources.Image;

/**
 * A provider of color for a ContainerComponent, either a pure color or an image
 */
public class ColorProvider implements Cloneable{

    private ColorRGBA color;
    private Image image;

    private ColorProvider() {
        color = null;
        image = null;
    }

    /**
     * creates a ColorProvider based on a pure Color
     *
     * @param color the provider's color
     * @return A ColorProvider with color defined as the given color
     */
    public static ColorProvider ofColor(ColorRGBA color) {
        ColorProvider result = new ColorProvider();
        result.color = color;
        return result;
    }

    /**
     * creates a ColorProvider based on an image
     *
     * @param image the Image Resource of the image
     * @return a ColorProvider based on the image id
     */
    public static ColorProvider ofImage(Image image) {
        ColorProvider result = new ColorProvider();
        result.image = image;
        return result;
    }

    public ColorProvider copy(){
        try {
            return (ColorProvider) clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @return the color maintained by the colorProvider
     * @throws UnsupportedOperationException if the colorProvider is not a color
     */
    public ColorRGBA getColor() throws UnsupportedOperationException {
        if (color == null) {
            throw new UnsupportedOperationException();
        }
        return color;
    }

    /**
     *
     * @return the image that this ColorProvider refers to
     * @throws UnsupportedOperationException if the ColorProvider does not refer
     * an image
     */
    public Image getImage()
            throws UnsupportedOperationException {
        if (image == null) {
            throw new UnsupportedOperationException();
        }
        return image;
    }

    public boolean isPureColor() {
        return color != null;
    }

    public boolean isImage() {
        return image != null;
    }

    @Override
    public String toString() {
        if (isPureColor()) {
            return color.toString();
        } else {
            return image.toString();
        }
    }
}
