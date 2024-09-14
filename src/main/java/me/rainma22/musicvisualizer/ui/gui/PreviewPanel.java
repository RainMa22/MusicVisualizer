package me.rainma22.musicvisualizer.ui.gui;

import me.rainma22.musicvisualizer.imageprocessor.AwtImageProcessor;
import me.rainma22.musicvisualizer.settings.SettingsChangeListener;
import me.rainma22.musicvisualizer.settings.SettingsEntry;
import me.rainma22.musicvisualizer.settings.SettingsManager;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.util.FastMath;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PreviewPanel extends JPanel implements SettingsChangeListener {
    private AwtImageProcessor previewProcessor;
    private SettingsManager settings;
    private Complex[] samples;
    private String foregroundImageString;
    private String backgroundImageString;

    public PreviewPanel() {
        super();
        settings = SettingsManager.getSettingsManager();
        settings.addListener(this);
        update(null, null);

        setMinimumSize(new Dimension(100, 100));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setDoubleBuffered(true);

    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Color bgColor = UIManager.getColor("Panel.background");
        g.setColor(bgColor);
        g.fillRect(0,0,getWidth(),getHeight());
        Graphics2D g2d = (Graphics2D) g;
        BufferedImage image = previewProcessor.processSample(samples);
        g2d.drawImage(image, 0, 0,
                image.getWidth(), image.getHeight(), this);
        g2d.dispose();
    }

    private void initPreviewProcessor() {
        int viewWidth = this.getWidth();
        int viewHeight = this.getHeight();
        if (viewWidth == 0 || viewHeight == 0) return;

        int width = (Integer) settings.getObj("width");
        int height = (Integer) settings.getObj("height");

        float scale = FastMath.min((float)viewWidth/width, (float) viewHeight/height);
        if (scale > 1) scale = 1;

        width = FastMath.round(width*scale);
        height = FastMath.round(height*scale);

        double threshold = (Double) settings.getObj("amplitude_threshold");
        BufferedImage foregroundImg = null;
        boolean changeForeground;
        if ((changeForeground = !settings.get("foreground_img").equals(foregroundImageString))){
            foregroundImg = (BufferedImage) settings.getObj("foreground_img");
            foregroundImageString = settings.get("foreground_img");
        }
        boolean changeBackground;
        BufferedImage backgroundImg = null;
        if ((changeBackground = !settings.get("background_img").equals(backgroundImageString))) {
            backgroundImg=(BufferedImage) settings.getObj("background_img");
            backgroundImageString = settings.get("background_img");
        }
        double rotationPerFrameTheta = (Double) settings.getObj("rotation_per_theta");
        String lineColor = settings.get("line_color_hex");
        int blurSize = (Integer) settings.getObj("blur_size");
        if (samples == null) {
            samples = new Complex[256];
            for (int i = 0; i < samples.length; i++) {
                samples[i] = new Complex(FastMath.random() * 32, 0);
            }
        }
        if (previewProcessor == null) {
            previewProcessor = new AwtImageProcessor(width, height, threshold, null, null, rotationPerFrameTheta);
        } else {
            previewProcessor.setWidth(width);
            previewProcessor.setHeight(height);
            previewProcessor.setThreshold(threshold);
            previewProcessor.setRotationPerFrameTheta(rotationPerFrameTheta);
        }
        previewProcessor.setLineColor(Color.decode(lineColor));
        if (changeForeground)
            previewProcessor.setForegroundImage(foregroundImg);

        if (changeBackground)
            previewProcessor.setBackgroundImageAndBlurSize(backgroundImg,blurSize);
        else
            previewProcessor.setBlurSize(blurSize);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        update(null,null);

    }

    @Override
    public void update(String UpdatedKey, SettingsEntry entry) {
        initPreviewProcessor();
        repaint();
    }
}
