
package me.rainma22.musicvisualizer.Image;

/**
 * represents a renderer of a intermediate Image
 */
public interface ImageRenderer {
    public void drawCircle(Circle c);
    public void drawIntermediateImage(IntermediateImage iimg);
    public void drawPoint(Point p);
    public void drawPolyLine(PolyLine pl);
    public void drawRectangle(Rectangle r);   
}
