package me.rainma22.musicvisualizer.ui.gui;

import me.rainma22.musicvisualizer.settings.SettingsEntry;
import me.rainma22.musicvisualizer.settings.SettingsManager;
import org.apache.commons.math3.util.FastMath;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Set;

public class SettingsPanel extends JPanel {
    public SettingsPanel(int numItems) {
        super();
        int size = (int) (FastMath.sqrt(numItems)) + 1;
        GridLayout layout = new GridLayout(numItems, 1);
        setLayout(layout);
    }

    private static JPanel newIntPanel(SettingsEntry entry){
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.X_AXIS);
        panel.setLayout(layout);
        String[] possibleValues = entry.getPossibleValues();
        int val = Integer.parseInt(entry.getValue());
        int min = val / 5;
        int max = val * 10;
        if (possibleValues != null && possibleValues.length >= 2) {
            try {
                min = Integer.parseInt(possibleValues[0]);
            } catch (NumberFormatException ignored) {
            }
            try {
                max = Integer.parseInt(possibleValues[1]);
            } catch (NumberFormatException ignored) {
            }
        }
        JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, val);
        JTextField textField = new JTextField(String.valueOf(val));
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                textField.setText(String.valueOf(slider.getValue()));
            }
        });
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    slider.setValue(Integer.parseInt(textField.getText()));
                } catch (NumberFormatException ignored) {
                }
            }
        });
        panel.add(slider);
        panel.add(textField);
        return panel;
    }

    private static JPanel newDoublePanel(SettingsEntry entry){
        String[] possibleValues = entry.getPossibleValues();
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.X_AXIS);
        panel.setLayout(layout);
        double val = Double.parseDouble(entry.getValue());
        double min = val / 5;
        double max = val * 10;
        if (possibleValues != null && possibleValues.length >= 2) {
            try {
                min = Double.parseDouble(possibleValues[0]);
            } catch (NumberFormatException ignored) {
            }
            try {
                max = Double.parseDouble(possibleValues[1]);
            } catch (NumberFormatException ignored) {
            }
        }
        JSlider slider = new JSlider(JSlider.HORIZONTAL,
                FastMath.round((float) min * 1024),
                FastMath.round((float) max * 1024),
                FastMath.round((float) val * 1024));
        JTextField textField = new JTextField(String.valueOf(val));
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                textField.setText(String.format("%.8f",slider.getValue()/1024d));
            }
        });
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    slider.setValue(FastMath.round(Float.parseFloat(textField.getText())*1024));
                } catch (NumberFormatException ignored) {
                }
            }
        });
        panel.add(slider);
        panel.add(textField);
        return panel;
    }

    private static JPanel newFolderPanel(SettingsEntry entry){
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.X_AXIS);
        panel.setLayout(layout);
        JTextField textField = new JTextField(entry.toString());
        JFileChooser chooser = new JFileChooser(new File(textField.getText()));
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        JButton chooserBtn = new JButton("Choose");
        chooserBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = chooser.showOpenDialog(panel);
                if (result == JFileChooser.APPROVE_OPTION) {
                    textField.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        panel.add(textField);
        panel.add(chooserBtn);
        return panel;
    }
    private static JPanel newFilePanel(SettingsEntry entry){
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.X_AXIS);
        panel.setLayout(layout);
        JTextField textField = new JTextField(entry.toString());
        JFileChooser chooser = new JFileChooser(new File(textField.getText()));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        JButton chooserBtn1 = new JButton("Choose");
        chooserBtn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = chooser.showOpenDialog(panel);
                if (result == JFileChooser.APPROVE_OPTION) {
                    textField.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        panel.add(textField);
        panel.add(chooserBtn1);
        return panel;
    }

    private static JPanel newColorPanel(SettingsEntry entry){
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.X_AXIS);
        panel.setLayout(layout);
        JTextField colorField = new JTextField(entry.toString());
        JButton colorBtn = new JButton();
        colorBtn.setBackground(Color.decode(colorField.getText()));
        colorBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(panel,
                        "Choose a color", Color.decode(colorField.getText()), false);
                colorField.setText(String.format("0x%6x", color.getRGB() & 0xFFFFFF));
                colorBtn.setForeground(color);
            }
        });
        panel.add(colorField);
        panel.add(colorBtn);
        return panel;
    }

    private static JPanel panelFromSetting(String key, SettingsEntry entry) {
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        panel.setLayout(layout);
        JLabel label = new JLabel(entry.getDescription() + ":");
//        label.setAlignmentX(JLabel.LEFT_ALIGNMENT);
//        label.setBorder(new LineBorder(Color.BLACK, 1));
        panel.add(label);
        switch (entry.getType()) {
            case FACTOR:
                panel.add(new JComboBox<String>(entry.getPossibleValues()));
                break;
            case INT:
                panel.add(newIntPanel(entry));
                break;
            case DOUBLE:
                panel.add(newDoublePanel(entry));
                break;
            case FOLDER:
                panel.add(newFolderPanel(entry));
                break;
            case FILE:
                panel.add(newFilePanel(entry));
                break;
            case COLOR:
                panel.add(newColorPanel(entry));
                break;
            case STRING:
            default:
                panel.add(new JTextField(entry.toString()));
        }
        return panel;
    }

    public static SettingsPanel fromSettings() {
        SettingsManager settings = SettingsManager.getSettingsManager();
        Set<String> keys = settings.keySet();
        SettingsPanel out = new SettingsPanel(keys.size());
        for (String key : keys) {
            out.add(panelFromSetting(key, settings.getEntry(key)));
        }
        return out;
    }
}
