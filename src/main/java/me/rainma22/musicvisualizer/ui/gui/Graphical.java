package me.rainma22.musicvisualizer.ui.gui;


import me.rainma22.musicvisualizer.VisualizationExporter;
import me.rainma22.musicvisualizer.imageprocessor.AwtImageProcessor;
import me.rainma22.musicvisualizer.settings.SettingsManager;
import me.rainma22.musicvisualizer.util.ProcessUtils;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Graphical extends JFrame {

    private JSplitPane splitPane, splitPane1;
    private SettingsPanel settingField;
    private PreviewPanel previewPanel;
    private FileBar inputFileBar;
    private JMenuBar menuBar;

    public Graphical(int width, int height) {
        super("Music Visualizer");
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem ffmpegPathItem = new JMenuItem("Select Path to FFmpeg");
        ffmpegPathItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FFmpegPopup().setVisible(true);
            }
        });
        fileMenu.add(ffmpegPathItem);
        menuBar.add(fileMenu);

        previewPanel = new PreviewPanel();
        settingField = SettingsPanel.fromSettings();

        inputFileBar = new FileBar("Input", "./");
        inputFileBar.setFileFilter(new FileNameExtensionFilter("Wave File(.wav)", "wav", "Wav", "WAV"));
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane1.setPreferredSize(new Dimension(width, height * 9 / 10));


        setSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));
        setMinimumSize(new Dimension(640, 360));
        setResizable(true);


        splitPane.setPreferredSize(new Dimension(width, height / 10 * 9));
        splitPane.setSize(width, height / 10 * 9);
        splitPane.setDividerLocation(.2);

        splitPane.setLeftComponent(settingField);
        splitPane.setRightComponent(previewPanel);
        splitPane1.setTopComponent(splitPane);
        splitPane1.setBottomComponent(inputFileBar);
        settingField.onExport((e) -> {
            File outFolder = new File("./output");
            outFolder.mkdirs();
            JFileChooser chooser = new OutputFileChooser(outFolder);
            int status = chooser.showSaveDialog(this);
            if (status == JFileChooser.APPROVE_OPTION) {
                File musicFile = new File(inputFileBar.getFilePath());
                VisualizationExporter exporter = new VisualizationExporter();
                SettingsManager settings = SettingsManager.getSettingsManager();
                try {
                    AwtImageProcessor processor = new AwtImageProcessor(
                            (Integer)settings.getObj("width"),
                            (Integer) settings.getObj("height"),
                            (Double) settings.getObj("amplitude_threshold"),
                            (BufferedImage) settings.getObj("foreground_img"),
                            (BufferedImage) settings.getObj("background_img"),
                            (Double) settings.getObj("rotation_per_theta"));
                    processor.setBlurSize((Integer) settings.getObj("blur_size"));
                    processor.setLineColor(Color.decode(settings.get("line_color_hex")));
                    exporter.export(musicFile.getAbsolutePath(),
                            (Integer) settings.getObj("fps"),
                            ProcessUtils.isProgramRunnable(settings.get("path_to_ffmpeg")),
                            chooser.getSelectedFile().getAbsolutePath(),
                            processor
                            );
                } catch (UnsupportedAudioFileException ex) {
                    JOptionPane.showMessageDialog(this, "Unsupported Audio!", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(this, "File Not Found!", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, ex, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setJMenuBar(menuBar);
        add(new FFmpegStatusLabel(this));
        add(splitPane1);
        splitPane1.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                splitPane1.setDividerLocation(.95f);
            }
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void start() {
        setVisible(true);
        revalidate();
    }

}
