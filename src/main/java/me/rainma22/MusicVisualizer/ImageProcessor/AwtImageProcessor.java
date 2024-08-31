package me.rainma22.MusicVisualizer.ImageProcessor;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * An image processor that takes a list of float and outputs them onto an image
 * using Java.awt.Image
 **/
public class AwtImageProcessor {
    private int width, height;
    private double threshold;
    private BufferedImage circleImage;
    private int frameIndex;
    private double rotationPerFrameTheta;
    private Ellipse2D circleClip;
    private Color lineColor;

    /**
     * Creates a new AwtImageProcessor, setting:
     * threshold to 0
     * lineColor to Black
     * circleImage to null
     * rotation per frame to Math.PI/60
     *
     * @param width  the desired Image Width
     * @param height the desired Image Height
     **/
    public AwtImageProcessor(int width, int height) {
        this(width, height, 0);
    }

    /**
     * Creates a new AwtImageProcessor, setting:
     * lineColor to Black
     * circleImage to null
     * rotation per frame to Math.PI/60
     *
     * @param width     the desired Image Width
     * @param height    the desired Image Height
     * @param threshold the amplitude threshold, sound will only render if they go pass this threshold
     **/
    public AwtImageProcessor(int width, int height, double threshold) {
        this(width, height, threshold, null, Math.PI / 60);
    }

    /**
     * Creates a new AwtImageProcessor, Setting
     *
     * @param width                 the desired Image Width
     * @param height                the desired Image Height
     * @param threshold             the amplitude threshold, sound will only render if they go pass this threshold
     * @param circleImage           Nullable, the circle image to be rendered by the ImageRender;
     * @param rotationPerFrameTheta the amount of foreground rotation per frame;
     **/
    public AwtImageProcessor(int width, int height, double threshold, BufferedImage circleImage, double rotationPerFrameTheta) {
        this.width = width;
        this.height = height;
        this.threshold = threshold;
        setCircleImage(circleImage);
        frameIndex = 0;
        setRotationPerFrameTheta(rotationPerFrameTheta);
        lineColor = Color.BLACK;
    }


    public void setCircleImage(BufferedImage circleImage) {
        this.circleImage = circleImage;
    }

    /**
     * sets the rotationPerFrameTheta
     * WARNING: may cause unexpected/interesting behavior when the rendering process has already started
     **/
    public void setRotationPerFrameTheta(double rotationPerFrameTheta) {
        this.rotationPerFrameTheta = rotationPerFrameTheta;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    /**
     * processes the given sample Frequency and sample Amplitude into an Image, and returns it
     *
     * @param sampleRe Sample Frequency
     * @param sampleIm Sample Amplitude
     */
    public BufferedImage processSample(double[] sampleRe, double[] sampleIm) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) image.getGraphics().create();
        g.fillRect(0, 0, width, height);
        int[] xPos = new int[sampleIm.length];
        int[] yPos = new int[sampleIm.length];

        float[] amp = new float[sampleIm.length];
        for (int i = 0; i < sampleRe.length; i++) {
//            amp[i] = (float) Math.abs(Math.atan(Math.sqrt(sampleRe[i]*sampleRe[i] + sampleIm[i] * sampleIm[i])));
            amp[i] = (float) Math.log(Math.sqrt(sampleRe[i] * sampleRe[i] + sampleIm[i] * sampleIm[i]) + 1);
            if (amp[i] == 0 && i != 0) amp[i] = amp[i - 1];
            else if (amp[i] == 0 && i != sampleRe.length - 1) amp[i] = amp[i + 1];

            amp[i] = (float) (amp[i] / (2 * Math.PI));

            if (amp[i] < threshold) amp[i] = (float) (threshold * 7 / 8);


            double theta = 2 * Math.PI / sampleRe.length * i;
            theta += frameIndex * rotationPerFrameTheta;

            double radius = (width / 5d + amp[i] * width / 2);
            xPos[i] = (int) (radius * Math.cos(theta) + .5);
            xPos[i] = (width - xPos[i]) / 2;
            yPos[i] = (int) (radius * Math.sin(theta) + .5);
            yPos[i] = (height - yPos[i]) / 2;
        }
        g.setColor(lineColor);
        g.drawPolyline(xPos, yPos, yPos.length);
        int circleDiameter = width / 5;
        int circleX = (width - circleDiameter) / 2;
        int circleY = (height - circleDiameter) / 2;
        if (this.circleImage == null) {
            g.setStroke(new BasicStroke((float) width / 192));
            g.setColor(Color.BLACK);
            g.drawOval(circleX, circleY, circleDiameter, circleDiameter);
        } else {
            if (circleClip == null) circleClip = new Ellipse2D.Double(circleX, circleY, circleDiameter, circleDiameter);
            AffineTransform at = new AffineTransform();
            double scale = Math.max((double) circleDiameter / circleImage.getWidth(), (double) circleDiameter / circleImage.getHeight());
            at.scale(scale, scale);
            double rotateAnchor = Math.min(circleImage.getWidth() / 2d, circleImage.getHeight() / 2d);
            at.rotate(frameIndex * rotationPerFrameTheta, rotateAnchor, rotateAnchor);

            AffineTransformOp operation = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
            g.setClip(circleClip);
            g.drawImage(circleImage, operation, (width - width / 5) / 2, (height - width / 5) / 2);
            g.setClip(null);
        }
        g.dispose();
        frameIndex++;
        return image;
    }


}
