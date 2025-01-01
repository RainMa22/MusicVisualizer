package me.rainma22.musicvisualizer.ui.gui;

import me.rainma22.intermediateimage.ColorRGBA;
import me.rainma22.intermediateimage.EffectApplier;
import me.rainma22.intermediateimage.IntermediateImage;
import me.rainma22.intermediateimage.Renderers.AwtImageRenderer;
import me.rainma22.intermediateimage.Renderers.SystemEffectApplier;
import me.rainma22.intermediateimage.Resources.Audio;
import me.rainma22.intermediateimage.Resources.Image;
import me.rainma22.intermediateimage.Resources.ResourceManager;
import me.rainma22.musicvisualizer.settings.SettingsChangeListener;
import me.rainma22.musicvisualizer.settings.SettingsEntry;
import me.rainma22.musicvisualizer.settings.SettingsManager;
import me.rainma22.musicvisualizer.util.ImageUtils;
import org.apache.commons.math3.util.FastMath;

import javax.swing.*;
import javax.swing.plaf.basic.BasicBorders;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.nio.file.Path;

public class PreviewPanel extends JPanel implements SettingsChangeListener {
    private IntermediateImage imageTemplate;
    private EffectApplier applier;
    private ResourceManager resMan;
    private final SettingsManager settings;
    private AwtImageRenderer renderer;
    private IntermediateImage finalImage;
    private int sampleSize = 2048;

    public PreviewPanel() {
        super();
        setBorder(BasicBorders.getButtonBorder());
        settings = SettingsManager.getSettingsManager();
        settings.addListener(this);
        update(null, null);

        setMinimumSize(new Dimension(100, 100));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setDoubleBuffered(true);

        imageTemplate = new IntermediateImage(1,1);
        resMan = imageTemplate.getResourceManager();
        applier = SystemEffectApplier.of(resMan);
        resMan.setAudio("Audio", new Audio(Path.of("test_16bitInt.wav")));
        renderer = new AwtImageRenderer(imageTemplate);
        finalImage = new IntermediateImage(1, 1);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Color bgColor = UIManager.getColor("Panel.background");
        g.setColor(bgColor);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int viewWidth = getWidth();
        int viewHeight = getHeight();

        finalImage.render(renderer);
        java.awt.Image image = renderer.finalizeImage();

//        int width = image.getWidth(null);
//        int height = image.getHeight(null);
//        AffineTransform at = new AffineTransform();
//        float scale = FastMath.min((float) viewWidth / width, (float) viewHeight / height);
//        float centerX = (viewWidth - width * scale) / 2;
//        float centerY = (viewHeight - height * scale) / 2;
//        at.scale(scale, scale);
//        at.translate(centerX, centerY);
        AffineTransform at = new AffineTransform();
        ((Graphics2D) g).drawImage(image, at, null);
    }

    private void initPreviewProcessor() {
        int viewWidth = this.getWidth();
        int viewHeight = this.getHeight();
        if (viewWidth == 0 || viewHeight == 0) return;

        int width = (Integer) settings.getObj(SettingsManager.KEY_WIDTH);
        int height = (Integer) settings.getObj(SettingsManager.KEY_HEIGHT);


        float scale = FastMath.min((float) viewWidth / width, (float) viewHeight / height);
        if (scale > 1) scale = 1;
//        float scale = 1;

        width = FastMath.round(width * scale);
        height = FastMath.round(height * scale);
        int blurSize = (Integer) settings.getObj("blur_size");

        String lineColor = settings.get("line_color_hex");

        imageTemplate = ImageUtils.getDefaultIntermediateImage(width, height,
                sampleSize,
                new ColorRGBA(Integer.decode(lineColor)));
        imageTemplate.setResourceManager(resMan);

        double threshold = (Double) settings.getObj(SettingsManager.KEY_AMPLITUDE_THRESHOLD);
        Image foregroundResource = (Image) settings.getObj(SettingsManager.KEY_FOREGROUND_IMG);
        Image backgroundResource = (Image) settings.getObj(SettingsManager.KEY_BACKGROUND_IMG);
        resMan.setImage(SettingsManager.KEY_FOREGROUND_IMG, foregroundResource);
        resMan.setImage(SettingsManager.KEY_BACKGROUND_IMG, backgroundResource);
        double rotationPerFrameTheta = (Double) settings.getObj(SettingsManager.KEY_ROTATION_THETA);
        renderer = new AwtImageRenderer(imageTemplate);
        finalImage = (IntermediateImage) imageTemplate.applyEffects(120, applier);
//        finalImage.setResourceManager(resMan);
        renderer = new AwtImageRenderer(finalImage);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        update(null, null);

    }

    @Override
    public void update(String UpdatedKey, SettingsEntry entry) {
        initPreviewProcessor();
        repaint();
    }
}
