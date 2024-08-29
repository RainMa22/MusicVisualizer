package me.rainma22.MusicVisualizer.ImageProcessor;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * An image processor that takes a list of float and outputs them onto an image
 * using Java.awt.Image
 **/
public class AwtImageProcessor {
    private int width, height;
    private double threshold;
    /**
     * Creates a new AwtImageProcessor
     *
     * @param width  the desired Image Width
     * @param height the desired Image Height
     **/
    public AwtImageProcessor(int width, int height) {
        this(width,height,0);
    }

    /**
     * Creates a new AwtImageProcessor
     *
     * @param width  the desired Image Width
     * @param height the desired Image Height
     * @param threshold  the amplitude threshold, sound will only render if they go pass this threshold
     **/
    public AwtImageProcessor(int width, int height,double threshold) {
        this.width = width;
        this.height = height;
        this.threshold = threshold;
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
            amp[i] = (float) Math.abs(Math.atan(Math.sqrt(sampleRe[i]*sampleRe[i] + sampleIm[i] * sampleIm[i])));

            if (amp[i] == 0 && i != 0) amp[i] = amp[i - 1];
            else if (amp[i] == 0 && i != sampleRe.length-1) amp[i] = amp[i + 1];

            if (i == amp.length-1) amp[i] = amp[i - 1];

            if (amp[i] < threshold) amp[i] = 0;

            amp[i] = (float) (amp[i]/(2*Math.PI));



            double theta = 2*Math.PI/sampleRe.length*i;
            double radius = (width/5 + amp[i] * width/2);
            xPos[i] = (int) (radius * Math.cos(theta) + .5);
            xPos[i] = (width - xPos[i]) / 2;
            yPos[i] = (int) (radius * Math.sin(theta) + .5);
            yPos[i] = (height - yPos[i]) / 2;
        }
        g.setColor(Color.BLUE);
        g.drawPolyline(xPos, yPos, yPos.length);
//        //DEBUG
//        g.setColor(Color.RED);
//        for (int i = 0; i < sampleRe.length; i++) {
//            amp[i] = (float) Math.atan(Math.sqrt(sampleRe[i]*sampleRe[i] + sampleIm[i] * sampleIm[i]));
//            amp[i] = (float) Math.max(0, amp[i]-threshold);
//
//            xPos[i] = (int) (((float)width/sampleRe.length)*i);
//            yPos[i] = (int) ((height)/2 - amp[i]*width/8);
//        }
//        g.drawPolyline(xPos, yPos, yPos.length);

        g.setStroke(new BasicStroke(10));
        g.setColor(Color.BLACK);

        g.drawOval((width - width / 5) / 2, (height - width / 5) / 2, width / 5, width / 5);

        g.dispose();
        return image;
    }


}
