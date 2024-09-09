package me.rainma22.musicvisualizer.ui.gui;

import me.rainma22.musicvisualizer.settings.SettingsManager;
import me.rainma22.musicvisualizer.util.ui.gui.GuiUtils;

import javax.swing.*;
import java.awt.*;

public class FFmpegPopup extends JFrame {
    public FFmpegPopup(){
        super("Select FFmpeg");
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(new JLabel("Path To FFmpeg: "));
        add(panel);
        add(GuiUtils.newFilePanel("path_to_ffmpeg", SettingsManager.getSettingsManager().getEntry("path_to_ffmpeg")));
        FFmpegStatusLabel statusLabel = new FFmpegStatusLabel(this);
        statusLabel.setPreferredSize(new Dimension(600, statusLabel.getFont().getSize()*3/2));
        add(statusLabel);
        pack();
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}
