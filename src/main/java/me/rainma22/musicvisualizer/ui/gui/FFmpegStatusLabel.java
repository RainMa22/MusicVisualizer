package me.rainma22.musicvisualizer.ui.gui;

import me.rainma22.musicvisualizer.settings.SettingsChangeListener;
import me.rainma22.musicvisualizer.settings.SettingsEntry;
import me.rainma22.musicvisualizer.settings.SettingsManager;
import me.rainma22.musicvisualizer.util.ProcessUtils;

import javax.swing.*;
import java.awt.*;

public class FFmpegStatusLabel extends JLabel implements SettingsChangeListener {
    private static final String DISABLED_STRING = "Please specify FFmpeg path in file > Select Path to FFmpeg! Using Pure Java instead(no Audio in video output)!";
    private static final String ENABLED_STRING = "FFmpeg is usable!";
    private Container parent;

    public FFmpegStatusLabel(Container parent) {
        super("", SwingConstants.CENTER);
        this.parent =parent;
//        setBorder(new LineBorder(Color.RED, 1));
        SettingsManager.getSettingsManager().addListener(this);
        update("path_to_ffmpeg", SettingsManager.getSettingsManager().getEntry("path_to_ffmpeg"));
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(0, y, parent.getWidth(), getFont().getSize() * 3 / 2);
        setText(getText());

    }

    @Override
    public void update(String UpdatedKey, SettingsEntry entry) {
        if (UpdatedKey.equals("path_to_ffmpeg")) {
            SwingUtilities.invokeLater(() -> {
                if (ProcessUtils.isProgramRunnable(entry.getValue())) {
                    setForeground(Color.GREEN.darker());
                    setText(ENABLED_STRING);
                } else {
                    setForeground(Color.RED);
                    setText(DISABLED_STRING);
                }
            });
        }
    }
}
