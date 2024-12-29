
package me.rainma22.intermediateimage.Renderers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import me.rainma22.intermediateimage.Circle;
import me.rainma22.intermediateimage.EffectApplier;
import me.rainma22.intermediateimage.ImageRenderer;
import me.rainma22.intermediateimage.IntermediateImage;
import me.rainma22.intermediateimage.Point;
import me.rainma22.intermediateimage.PolyLine;
import me.rainma22.intermediateimage.Rectangle;
import me.rainma22.intermediateimage.Resources.ResourceManager;

/**
 * renders Intermediate Images into BufferedImages
 */
public class AwtImageRenderer extends ImageRenderer {
    
    private BufferedImage image;
    private Graphics2D g2d;
    
    public AwtImageRenderer(IntermediateImage intermediateImage){
        super(SystemEffectApplier.of(intermediateImage.getResourceManager()));
        int width = intermediateImage.getWidth();
        int height = intermediateImage.getHeight();
        image = new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);
        g2d = image.createGraphics();
    }

    @Override
    public void drawCircle(Circle c) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void drawIntermediateImage(IntermediateImage iimg) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void drawPoint(Point p) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void drawPolyLine(PolyLine pl) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void drawRectangle(Rectangle r) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
