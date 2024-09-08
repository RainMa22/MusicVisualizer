package me.rainma22.musicvisualizer.imageprocessor;

import me.rainma22.musicvisualizer.imageprocessor.blur.GaussianBlur;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.util.FastMath;

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
    private BufferedImage foregroundImage;
    private int frameIndex;
    private double rotationPerFrameTheta;
    private Color lineColor;
    private BufferedImage backgroundImage;

    private BufferedImage blurredImage;
    private int blurSize;

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
        this(width, height, threshold, null, null, Math.PI / 60);
    }


    /**
     * Creates a new AwtImageProcessor, Setting
     *
     * @param width                 the desired Image Width
     * @param height                the desired Image Height
     * @param threshold             the amplitude threshold, sound will only render if they go pass this threshold
     * @param foregroundImage       Nullable, the circle image to be rendered by the ImageRender;
     * @param rotationPerFrameTheta the amount of foreground rotation per frame;
     **/
    public AwtImageProcessor(int width, int height, double threshold, BufferedImage foregroundImage, BufferedImage backgroundImage, double rotationPerFrameTheta) {
        this.width = width;
        this.height = height;
        this.threshold = threshold;
        setForegroundImage(foregroundImage);
        frameIndex = 0;
        setRotationPerFrameTheta(rotationPerFrameTheta);
        lineColor = Color.BLACK;
        blurSize = 0;
        setBackgroundImage(backgroundImage);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public void setFrameIndex(int frameIndex) {
        this.frameIndex = frameIndex;
    }

    public void setForegroundImage(BufferedImage foregroundImage) {
        this.foregroundImage = foregroundImage;
    }

    public void setBackgroundImage(BufferedImage backgroundImage) {
        if (backgroundImage !=null && backgroundImage.equals(this.backgroundImage)) return;
        this.backgroundImage = backgroundImage;
        if (backgroundImage != null) {
            double scale = Math.max((double) width / backgroundImage.getWidth(), (double) height / backgroundImage.getHeight());
            int newWidth = (int) (backgroundImage.getWidth() * scale);
            int newHeight =(int) (backgroundImage.getHeight() * scale);
            this.backgroundImage = new BufferedImage(newWidth, newHeight, backgroundImage.getType());
            Graphics2D g = this.backgroundImage.createGraphics();
            g.drawImage(backgroundImage,0,0,newWidth,newHeight,null);
            g.dispose();
        }
        setBlurSize(blurSize, true);
    }

    public void setBackgroundImageAndBlurSize(BufferedImage backgroundImage, int blurSize) {
        if (backgroundImage !=null && backgroundImage.equals(this.backgroundImage))
            setBlurSize(blurSize,false);
        this.backgroundImage = backgroundImage;
        if (backgroundImage != null) {
            double scale = Math.max((double) width / backgroundImage.getWidth(), (double) height / backgroundImage.getHeight());
            int newWidth = (int) (backgroundImage.getWidth() * scale);
            int newHeight =(int) (backgroundImage.getHeight() * scale);
            this.backgroundImage = new BufferedImage(newWidth, newHeight, backgroundImage.getType());
            Graphics2D g = this.backgroundImage.createGraphics();
            g.drawImage(backgroundImage,0,0,newWidth,newHeight,null);
            g.dispose();
        }
        setBlurSize(blurSize, true);
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
     * sets the blur size and also updates the blur image
     **/
    public void setBlurSize(int blurSize) {
        setBlurSize(blurSize, false);
    }
    private void setBlurSize(int blurSize, boolean forceChange) {
        if (!forceChange && (blurSize == this.blurSize)) return;
        this.blurSize = blurSize;
        if (backgroundImage != null) {
            GaussianBlur blur = new GaussianBlur(blurSize);
            if (blurredImage !=null &&
                    blurredImage.getWidth() == backgroundImage.getWidth() &&
                    blurredImage.getHeight() == backgroundImage.getHeight())
                blurredImage = blur.filter(backgroundImage,blurredImage);
            else
                blurredImage = blur.filter(backgroundImage,null);
        }
    }

    /**
     * processes the given sample Frequency and sample Amplitude into an Image, and returns it
     *
     * @param sampleData an Array of FFTed result
     */
    public BufferedImage processSample(Complex[] sampleData) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = (Graphics2D) image.getGraphics().create();
        if (blurredImage != null) {
            AffineTransform at = new AffineTransform();
            double scale = Math.max((double) width / blurredImage.getWidth(), (double) height / blurredImage.getHeight());
            at.scale(scale, scale);
            AffineTransformOp operation = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
            g.drawImage(blurredImage,operation,0,0);
        }
//        g.fillRect(0, 0, width, height);

        int[] xPos = new int[sampleData.length];
        int[] yPos = new int[sampleData.length];

        float[] amp = new float[sampleData.length];
        for (int i = 0; i < sampleData.length; i++) {
//            amp[i] = (float) Math.abs(Math.atan(Math.sqrt(sampleRe[i]*sampleRe[i] + sampleIm[i] * sampleIm[i])));
            double real = sampleData[i].getReal();
            double imaginary = sampleData[i].getImaginary();
            amp[i] = (float) FastMath.log(FastMath.sqrt(real*real + imaginary*imaginary) + 1);
            if (amp[i] == 0 && i != 0) amp[i] = amp[i - 1];
            else if (amp[i] == 0 && i != sampleData.length - 1) amp[i] = amp[i + 1];

            amp[i] = (float) (amp[i] / (2 * Math.PI));

            if (amp[i] < threshold) amp[i] = (float) (threshold * 7 / 8);


            double theta = 2 * Math.PI / sampleData.length * i;
            theta += frameIndex * rotationPerFrameTheta;

            double radius = (width / 5d + amp[i] * width / 2);
            xPos[i] = (int) (radius * Math.cos(theta) + .5);
            xPos[i] = (width - xPos[i]) / 2;
            yPos[i] = (int) (radius * Math.sin(theta) + .5);
            yPos[i] = (height - yPos[i]) / 2;
        }
        g.setColor(lineColor);
        g.drawPolygon(xPos, yPos, yPos.length);
        int circleDiameter = width / 5;
        int circleX = (width - circleDiameter) / 2;
        int circleY = (height - circleDiameter) / 2;
        if (this.foregroundImage == null) {
            g.setStroke(new BasicStroke((float) width / 192));
            g.setColor(Color.BLACK);
            g.drawOval(circleX, circleY, circleDiameter, circleDiameter);
        } else {
            Ellipse2D circleClip = new Ellipse2D.Float(circleX, circleY, circleDiameter, circleDiameter);
            AffineTransform at = new AffineTransform();
            float scale = FastMath.max((float) circleDiameter / foregroundImage.getWidth(), (float) circleDiameter / foregroundImage.getHeight());
            at.scale(scale, scale);
            float rotateAnchor = FastMath.min(foregroundImage.getWidth() / 2f, foregroundImage.getHeight() / 2f);
            at.rotate(frameIndex * rotationPerFrameTheta, rotateAnchor, rotateAnchor);

            AffineTransformOp operation = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
            g.setClip(circleClip);
            g.drawImage(foregroundImage, operation, (width - width / 5) / 2, (height - width / 5) / 2);
            g.setClip(null);
        }
        g.dispose();
        frameIndex++;
        return image;
    }


}
