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
    private JPanel wrapperPanel;
    private SettingsManager settings;
    private Canvas imagePreview;
    private Complex[] samples;

    public PreviewPanel() {
        super();
        settings = SettingsManager.getSettingsManager();
        settings.addListener(this);
        update(null, null);

        setMinimumSize(new Dimension(100, 100));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        wrapperPanel = new JPanel();
        wrapperPanel.setSize(this.getSize());
        imagePreview = new Canvas() {
            public void paint(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.drawImage(previewProcessor.processSample(samples), 0, 0, PreviewPanel.this.getWidth(), PreviewPanel.this.getHeight(), this);
                g2d.dispose();
            }
        };
        imagePreview.setSize(this.getSize());
        imagePreview.createBufferStrategy(1);
        wrapperPanel.add(imagePreview);
        wrapperPanel.setToolTipText("Preview");
        add(wrapperPanel);

    }

    private void initPreviewProcessor() {
        int width = (Integer) settings.getObj("width");
        int height = (Integer) settings.getObj("height");
        double threshold = (Double) settings.getObj("amplitude_threshold");
        BufferedImage foregroundImg = (BufferedImage) settings.getObj("foreground_img");
        BufferedImage backgroundImg = (BufferedImage) settings.getObj("background_img");
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
            previewProcessor = new AwtImageProcessor(width, height, threshold, foregroundImg, backgroundImg, rotationPerFrameTheta);
        } else {
            previewProcessor.setWidth(width);
            previewProcessor.setHeight(height);
            previewProcessor.setThreshold(threshold);
            previewProcessor.setForegroundImage(foregroundImg);
            previewProcessor.setBackgroundImage(backgroundImg);
            previewProcessor.setRotationPerFrameTheta(rotationPerFrameTheta);
        }
        previewProcessor.setLineColor(Color.decode(lineColor));
        previewProcessor.setBlurSize(blurSize);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        wrapperPanel.setSize(width, height);
        wrapperPanel.setMaximumSize(new Dimension(width, height));
        imagePreview.setSize(width, height);
        imagePreview.repaint();

    }

    @Override
    public void update(String UpdatedKey, SettingsEntry entry) {
        initPreviewProcessor();
        if (imagePreview != null) imagePreview.repaint();
    }
}
