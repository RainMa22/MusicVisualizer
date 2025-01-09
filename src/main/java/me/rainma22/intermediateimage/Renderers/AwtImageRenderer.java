package me.rainma22.intermediateimage.Renderers;

import me.rainma22.intermediateimage.Component;
import me.rainma22.intermediateimage.Point;
import me.rainma22.intermediateimage.Rectangle;
import me.rainma22.intermediateimage.*;
import me.rainma22.intermediateimage.Resources.Image;
import me.rainma22.musicvisualizer.imageprocessor.blur.GaussianBlur;
import org.apache.commons.math3.util.FastMath;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * renders Intermediate Images into BufferedImages
 */
public class AwtImageRenderer extends ImageRenderer<BufferedImage> {

    private BufferedImage image;
    private Graphics2D g2d;
    private SystemEffectApplier sysApplier;

    public AwtImageRenderer(SystemEffectApplier applier, int width, int height) {
        super(applier);
        sysApplier = applier;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = image.createGraphics();
    }

    /**
     * creates a AwtImageRenderer based on the parameters of the given intermediate image
     * <br>
     * in detail:
     * <br>
     * creates the EffectApplier from the image's ResourceManager
     * <br>
     * the internal buffer will be created using the intermediateImage's width and height;
     * <br>
     * HOWEVER! this does not draw the given intermediateImage
     *
     * @param intermediateImage the intermediateImage with the needed parameters.
     */
    private AwtImageRenderer(IntermediateImage intermediateImage) {
        super(SystemEffectApplier.of(intermediateImage.getResourceManager()));
        sysApplier = (SystemEffectApplier) super.applier;
        int width = intermediateImage.getWidth();
        int height = intermediateImage.getHeight();
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = image.createGraphics();
    }

    /**
     * in detail:
     * <br>
     * creates the EffectApplier from the image's ResourceManager
     * <br>
     * the internal buffer will be created using the intermediateImage's width and height;
     * <br>
     * HOWEVER! this does not draw the given intermediateImage
     *
     * @param iimg the intermediateImage with the needed parameters.
     * @return a AwtImageRenderer based on the parameters of the given intermediate image
     */
    public static AwtImageRenderer fromImageParam(IntermediateImage iimg) {
        return new AwtImageRenderer(iimg);
    }

    @Override
    public void drawCircle(Circle c) {
        ColorRGBA strokeColor = c.getStrokeColor_rgba();
        Color stroke = new Color(strokeColor.getRed(), strokeColor.getGreen(),
                strokeColor.getBlue(), strokeColor.getAlpha());
        int strokeSize = c.getStrokeSize_px();

        int circleDiameter = (int) (c.getRadius() * 2);
        int width = circleDiameter;
        int height = circleDiameter;
        Ellipse2D circleClip = new Ellipse2D.Float(c.getX(), c.getY(),
                circleDiameter, circleDiameter);

        if (c.getBackgroundColorProvider().isImage()) {
            BufferedImage toDraw = new BufferedImage(width,height,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d2 = toDraw.createGraphics();

            AffineTransform at = new AffineTransform();
            try {
                Image image = c.getBackgroundColorProvider().getImage();
                BufferedImage bi = (BufferedImage) sysApplier.getResourceLoader().loadImage(image);
                float scale = FastMath.max((float) width / bi.getWidth(null),
                        (float) height / bi.getHeight(null));
                int x = (int) ((width - bi.getWidth(null) * scale) / 2);
                int y = (int) ((height - bi.getHeight(null) * scale) / 2);
                at.scale(scale, scale);
                at.translate(x, y);

                g2d2.drawImage(bi, at, null);
                g2d2.dispose();
//                GaussianBlur blur = GaussianBlur.ofSize(c.getBackgroundGaussianBlurSize());
//                blur.filter(toDraw,toDraw);

            } catch (IOException ex) {
                Logger.getLogger(AwtImageRenderer.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
            g2d.setClip(circleClip);
            g2d.drawImage(toDraw, c.getX(), c.getY(), null);

        } else {//c.getBackgroundColorProvider().isColor()
            ColorRGBA backgroundColor = c.getBackgroundColorProvider().getColor();
            Color background = new Color(backgroundColor.getRed(),
                    backgroundColor.getGreen(),
                    backgroundColor.getBlue(),
                    backgroundColor.getAlpha());
            g2d.setColor(background);
            g2d.setClip(circleClip);
            g2d.fillOval(c.getX(), c.getY(), circleDiameter, circleDiameter);
        }
        for (Component child : c.getChildren()) {
            child.render(this);
        }
        g2d.setClip(null);
        g2d.setStroke(new BasicStroke(strokeSize));
        g2d.setColor(stroke);
        int outlineX = c.getX() - strokeSize;
        int outlineY = c.getY() - strokeSize;
        int outlineDiameter = (int) (c.getRadius() + (strokeSize * 2));
        g2d.drawOval(outlineX, outlineY, outlineDiameter, outlineDiameter);
    }

    @Override
    public void drawIntermediateImage(IntermediateImage iimg) {
        SystemEffectApplier effectApplier = SystemEffectApplier.of(iimg.getResourceManager());
        GaussianBlur blur = GaussianBlur.ofSize(iimg.getGaussianBlurSize());
        int width = iimg.getWidth();
        int height = iimg.getHeight();
        AwtImageRenderer renderer = new AwtImageRenderer(effectApplier, width, height);
        renderer.drawRectangle(iimg);
        BufferedImage image = renderer.finalizeImage();
        image = blur.filter(image, null);
        g2d.drawImage(image, iimg.getX(), iimg.getY(), null);
    }

    @Override
    public void drawPoint(Point p) {
        return;
    }

    @Override
    public void drawPolyLine(PolyLine pl) {
        ColorRGBA strokeColor = pl.getStrokeColor_rgba();
        Color stroke = new Color(strokeColor.getRed(), strokeColor.getGreen(),
                strokeColor.getBlue(), strokeColor.getAlpha());
        int strokeSize = pl.getStrokeSize_px();

        int[] xs = new int[pl.getChildren().size()];
        int[] ys = new int[xs.length];
        for (int i = 0; i < pl.getChildren().size(); i++) {
            Component child = pl.getChildren().get(i);
            child.render(this);
            xs[i] = child.getX();
            ys[i] = child.getY();
        }
        g2d.setStroke(new BasicStroke(strokeSize));
        g2d.setColor(stroke);
        g2d.drawPolyline(xs, ys, xs.length);
    }

    @Override
    public void drawRectangle(Rectangle r) {
        ColorRGBA strokeColor = r.getStrokeColor_rgba();
        Color stroke = new Color(strokeColor.getRed(), strokeColor.getGreen(),
                strokeColor.getBlue(), strokeColor.getAlpha());
        int strokeSize = r.getStrokeSize_px();

        int width = r.getWidth();
        int height = r.getHeight();
        Rectangle2D rectangleClip = new Rectangle2D.Float(r.getX(), r.getY(),
                width, height);

        if (r.getBackgroundColorProvider().isImage()) {
            BufferedImage toDraw = new BufferedImage(width,height,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d2 = toDraw.createGraphics();



            AffineTransform at = new AffineTransform();
            try {
                Image image = r.getBackgroundColorProvider().getImage();
                BufferedImage bi = (BufferedImage) sysApplier.getResourceLoader().loadImage(image);
                float scale = FastMath.max((float) width / bi.getWidth(null),
                        (float) height / bi.getHeight(null));
                int x = (int) ((width - bi.getWidth(null) * scale) / 2);
                int y = (int) ((height - bi.getHeight(null) * scale) / 2);
                at.scale(scale, scale);
                at.translate(x, y);

                g2d2.drawImage(bi, at, null);
                g2d2.dispose();
                GaussianBlur blur = GaussianBlur.ofSize(r.getBackgroundGaussianBlurSize());
                blur.filter(toDraw,toDraw);

            } catch (IOException ex) {
                Logger.getLogger(AwtImageRenderer.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }



            g2d.setClip(rectangleClip);
            g2d.drawImage(toDraw,r.getX(),r.getY(), null);

        } else {//c.getBackgroundColorProvider().isColor()
            ColorRGBA backgroundColor = r.getBackgroundColorProvider().getColor();
            Color background = new Color(backgroundColor.getRed(),
                    backgroundColor.getGreen(),
                    backgroundColor.getBlue(),
                    backgroundColor.getAlpha());
            g2d.setColor(background);
            g2d.setClip(rectangleClip);
            g2d.fillRect(r.getX(), r.getY(), width, height);
        }
        for (Component child : r.getChildren()) {
            child.render(this);
        }
        g2d.setClip(null);
//        g2d.setStroke(new BasicStroke(5));
//        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(strokeSize));
        g2d.setColor(stroke);
        int outlineX = r.getX() - strokeSize;
        int outlineY = r.getY() - strokeSize;
        int outlineWidth = width + strokeSize * 2;
        int outlineHeight = height + strokeSize * 2;

        g2d.drawRect(outlineX, outlineY, outlineWidth, outlineHeight);

    }

    @Override
    public BufferedImage finalizeImage() {
        g2d.dispose();
        return image;
    }

}
