package me.rainma22.musicvisualizer.ui.gui;

import me.rainma22.musicvisualizer.SettingsManager;
import org.apache.commons.math3.util.FastMath;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class SettingsPanel extends JPanel {
    public SettingsPanel(int numItems){
        super();
        int size = (int)(FastMath.sqrt(numItems)) + 1;
        GridLayout layout = new GridLayout(size,size);
        setLayout(layout);
    }

    public static SettingsPanel fromSettings(){
        SettingsManager settings = SettingsManager.getSettingsManager();
        Set<String> keys = settings.keySet();
        SettingsPanel out = new SettingsPanel(keys.size());
        for (String key: keys){
            JPanel panel = new JPanel();
            BoxLayout layout = new BoxLayout(panel,BoxLayout.Y_AXIS);
            panel.setLayout(layout);
            panel.add(new JLabel(key, SwingConstants.CENTER));
            panel.add(new JTextField(settings.get(key)));
            out.add(panel);
        }
        return out;
    }
}
