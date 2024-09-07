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
        GridLayout layout = new GridLayout(size, size);
        setLayout(layout);
    }

    private static JPanel panelFromSetting(String key, SettingsEntry entry) {
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        panel.setLayout(layout);
        panel.add(new JLabel(entry.getDescription(), SwingConstants.CENTER));
        String[] possibleValues = entry.getPossibleValues();
        switch (entry.getType()) {
            case FACTOR:
                panel.add(new JComboBox<String>(entry.getPossibleValues()));
                break;
            case INT:
            case DOUBLE:
//                int val = Integer.parseInt(entry.getValue());
                int val = (int) (Double.parseDouble(entry.getValue())+.5);
                int min = val / 5;
                int max = val * 10;
                if (possibleValues != null && possibleValues.length >= 2) {
                    try {
                        min = (int)(Double.parseDouble((possibleValues[0]))+.5);
                    } catch (NumberFormatException ignored) {
                    }
                    try {
                        max = (int)(Double.parseDouble(possibleValues[1]) + .5);
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
                break;
            case FOLDER:
                JTextField textField1 = new JTextField(entry.toString());
                JFileChooser chooser = new JFileChooser(new File(textField1.getText()));
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                JButton chooserBtn = new JButton("Choose");
                chooserBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int result = chooser.showOpenDialog(panel);
                        if (result == JFileChooser.APPROVE_OPTION) {
                            textField1.setText(chooser.getSelectedFile().getAbsolutePath());
                        }
                    }
                });
                panel.add(textField1);
                panel.add(chooserBtn);
                break;
            case FILE:
                JTextField textField2 = new JTextField(entry.toString());
                JFileChooser chooser1 = new JFileChooser(new File(textField2.getText()));
                chooser1.setFileSelectionMode(JFileChooser.FILES_ONLY);
                JButton chooserBtn1 = new JButton("Choose");
                chooserBtn1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int result = chooser1.showOpenDialog(panel);
                        if (result == JFileChooser.APPROVE_OPTION) {
                            textField2.setText(chooser1.getSelectedFile().getAbsolutePath());
                        }
                    }
                });
                panel.add(textField2);
                panel.add(chooserBtn1);
                break;
            case COLOR:
                JTextField colorField = new JTextField(entry.toString());
                JButton colorBtn = new JButton();
                colorBtn.setBackground(Color.decode(colorField.getText()));
                colorBtn.setPreferredSize(new Dimension(20, 20));
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
                break;
//            case DOUBLE:
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
