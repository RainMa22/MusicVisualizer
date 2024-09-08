package me.rainma22.musicvisualizer.ui.gui;

import me.rainma22.musicvisualizer.settings.EntryType;
import me.rainma22.musicvisualizer.settings.SettingsEntry;
import me.rainma22.musicvisualizer.settings.SettingsManager;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.math3.util.FastMath;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.util.Collection;

public class SettingsPanel extends JPanel {
    public SettingsPanel(int numItems) {
        super();
        int size = (int) (FastMath.sqrt(numItems)) + 1;
        GridLayout layout = new GridLayout(numItems, 1);
        setLayout(layout);
    }

    private static JPanel newIntPanel(String key, SettingsEntry entry) {
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
                SwingUtilities.invokeLater(() -> {
                    textField.setText(String.valueOf(slider.getValue()));
                });
            }
        });
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    String text = textField.getText();
                    int value = Integer.parseInt(text);
                    if (value >= slider.getMinimum()) {
                        slider.setValue(value);
                        SettingsManager.getSettingsManager().put(key, text);
                    }

                } catch (NumberFormatException ignored) {
                    SwingUtilities.invokeLater(() -> {
                        textField.setText(String.valueOf(slider.getValue()));
                    });
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                //forcefully update text
                textField.setText("inserting text");
                SettingsManager.getSettingsManager().notifyUpdate(key);
            }
        });
        panel.add(slider);
        panel.add(textField);
        return panel;
    }

    private static JPanel newDoublePanel(String key, SettingsEntry entry) {
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
                FastMath.round((float) min * 1000),
                FastMath.round((float) max * 1000),
                FastMath.round((float) val * 1000));
        JTextField textField = new JTextField(String.valueOf(val));
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                SwingUtilities.invokeLater(() -> {
                    textField.setText(String.format("%.3f", slider.getValue() / 1000d));
                });
            }
        });
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    String text = textField.getText();
                    if (!text.isEmpty()) {
                        int sliderVal = FastMath.round(Float.parseFloat(text) * 1000);
                        if (sliderVal >= slider.getMinimum()) {
                            slider.setValue(sliderVal);
                            SettingsManager.getSettingsManager().put(key, text);
                        }
                    }
                } catch (NumberFormatException ignored) {

                    SwingUtilities.invokeLater(() -> {
                        textField.setText(String.format("%.3f", slider.getValue() / 1000d));
                    });
                }
            }


            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                //forcefully update text
                textField.setText("inserting text");
                SettingsManager.getSettingsManager().notifyUpdate(key);
            }
        });
        panel.add(slider);
        panel.add(textField);
        return panel;
    }

    private static JPanel newFolderPanel(String key, SettingsEntry entry) {
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
                    String folderPath = chooser.getSelectedFile().getAbsolutePath();
                    textField.setText(folderPath);
                    SettingsManager.getSettingsManager().put(key, folderPath);
                }
            }
        });
        panel.add(textField);
        panel.add(chooserBtn);
        return panel;
    }

    private static JPanel newFilePanel(String key, SettingsEntry entry) {
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.X_AXIS);
        panel.setLayout(layout);
        EntryType type = entry.getType();
        JTextField textField = new JTextField(entry.toString());
        JFileChooser chooser = new JFileChooser(new File(textField.getText()));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (type == EntryType.EXECUTABLE) {
            if (SystemUtils.IS_OS_WINDOWS) {
                chooser.setFileFilter(new FileNameExtensionFilter("Executable file", "exe"));
            }
        } else if (type == EntryType.IMAGE) {
            String[][] accepted = new String[][]{
                    new String[]{"jpg", "jpeg"},
                    new String[]{"png"},
                    new String[]{"webp"},
                    new String[]{"bmp"}};
            String[] acceptedName = new String[]{
                    "JPEG file (.jpg) (.jpeg)",
                    "Portable Network Graphics (.png)",
                    "WEBP image (.webp)",
                    "Bitmap file (.bmp)"};
            for (int i = 0; i < accepted.length; i++) {
                if (i == 0)
                    chooser.setFileFilter(new FileNameExtensionFilter(acceptedName[i], accepted[i]));
                else
                    chooser.addChoosableFileFilter(new FileNameExtensionFilter(acceptedName[i], accepted[i]));
            }
        }

        JButton chooserBtn1 = new JButton("Choose");
        chooserBtn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = chooser.showOpenDialog(panel);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String filePath = chooser.getSelectedFile().getAbsolutePath();
                    textField.setText(filePath);
                    SettingsManager.getSettingsManager().put(key, filePath);
                }
            }
        });
        panel.add(textField);
        panel.add(chooserBtn1);
        return panel;
    }

    private static JPanel newColorPanel(String key, SettingsEntry entry) {
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.X_AXIS);
        panel.setLayout(layout);
        JTextField colorField = new JTextField(entry.toString());
        colorField.setEnabled(false);
        JButton colorBtn = new JButton();
        colorBtn.setBackground(Color.decode(colorField.getText()));
        colorBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(panel,
                        "Choose a color", Color.decode(colorField.getText()), false);
                String colorText = String.format("0x%6x", color.getRGB() & 0xFFFFFF);
                SettingsManager.getSettingsManager().put(key, colorText);
                colorField.setText(colorText);
                colorBtn.setBackground(color);
            }
        });
        panel.add(colorField);
        panel.add(colorBtn);
        return panel;
    }

    private static JPanel newFactorPanel(String key, SettingsEntry entry) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JComboBox<String> comboBox = new JComboBox<>(entry.getPossibleValues());
        comboBox.addActionListener((e) -> {
            SettingsManager.getSettingsManager().put(key, String.valueOf(comboBox.getSelectedItem()));
        });

        panel.add(comboBox);
        return panel;
    }

    private static JPanel panelFromSetting(String key, SettingsEntry entry) {
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        panel.setLayout(layout);
        JLabel label = new JLabel(entry.getDescription() + ":");
        panel.add(label);
        switch (entry.getType()) {
            case FACTOR:
                panel.add(newFactorPanel(key, entry));
                break;
            case INT:
                panel.add(newIntPanel(key, entry));
                break;
            case DOUBLE:
                panel.add(newDoublePanel(key, entry));
                break;
            case FOLDER:
                panel.add(newFolderPanel(key, entry));
                break;
            case IMAGE:
            case EXECUTABLE:
                panel.add(newFilePanel(key, entry));
                break;
            case COLOR:
                panel.add(newColorPanel(key, entry));
                break;
            case STRING:
            default:
                panel.add(new JTextField(entry.toString()));
                break;
        }
        return panel;
    }

    public static SettingsPanel fromSettings() {
        SettingsManager settings = SettingsManager.getSettingsManager();
        Collection<String> keys = settings.keyCollection();
        SettingsPanel out = new SettingsPanel(keys.size());
        for (String key : keys) {
            out.add(panelFromSetting(key, settings.getEntry(key)));
        }
        return out;
    }
}
