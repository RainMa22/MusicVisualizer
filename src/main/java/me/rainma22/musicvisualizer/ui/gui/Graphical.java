package me.rainma22.musicvisualizer.ui.gui;


import com.formdev.flatlaf.FlatLightLaf;
import me.rainma22.musicvisualizer.exporter.ExportStatusListener;
import me.rainma22.musicvisualizer.exporter.VisualizationExporter;
import me.rainma22.musicvisualizer.imageprocessor.AwtImageProcessor;
import me.rainma22.musicvisualizer.settings.SettingsManager;
import me.rainma22.musicvisualizer.util.ProcessUtils;
import org.apache.commons.math3.util.FastMath;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Graphical extends JFrame implements ExportStatusListener {

    private JSplitPane splitPane, splitPane1;
    private SettingsPanel settingField;
    private PreviewPanel previewPanel;
    private FileBar inputFileBar;
    private JMenuBar menuBar;
    private ProgressPanel progressPanel;

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
        progressPanel = new ProgressPanel(0, 100);
        progressPanel.setPreferredSize(new Dimension(width, height / 10));



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

                AwtImageProcessor processor = new AwtImageProcessor(
                        (Integer) settings.getObj("width"),
                        (Integer) settings.getObj("height"),
                        (Double) settings.getObj("amplitude_threshold"),
                        (BufferedImage) settings.getObj("foreground_img"),
                        (BufferedImage) settings.getObj("background_img"),
                        (Double) settings.getObj("rotation_per_theta"));
                processor.setBlurSize((Integer) settings.getObj("blur_size"));
                processor.setLineColor(Color.decode(settings.get("line_color_hex")));

                settingField.setEnabled(false);
                splitPane1.setBottomComponent(progressPanel);
                exporter.addListener(this);
                new Thread(() -> {
                    try {
                        exporter.export(musicFile.getAbsolutePath(),
                                (Integer) settings.getObj("fps"),
                                ProcessUtils.isProgramRunnable(settings.get("path_to_ffmpeg")),
                                chooser.getSelectedFile().getAbsolutePath(),
                                processor
                        );
                    } catch (UnsupportedAudioFileException ex) {
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(this, "Unsupported Audio!", "Error", JOptionPane.ERROR_MESSAGE);
                            exporter.notifyStatus(-1, -1);
                        });
                    } catch (FileNotFoundException ex) {
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(this, "File Not Found!", "Error", JOptionPane.ERROR_MESSAGE);
                            exporter.notifyStatus(-1, -1);
                        });
                    } catch (IOException ex) {
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(this, ex, "Error", JOptionPane.ERROR_MESSAGE);
                            exporter.notifyStatus(-1, -1);
                        });
                    }
                }).start();
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
        splitPane1.addContainerListener(new ContainerAdapter() {
            @Override
            public void componentAdded(ContainerEvent e) {
                super.componentAdded(e);
                splitPane1.setDividerLocation(.95f);
            }

            @Override
            public void componentRemoved(ContainerEvent e) {
                super.componentRemoved(e);
                splitPane1.setDividerLocation(.95f);
            }
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void start() {
        setVisible(true);
        revalidate();
    }

    @Override
    public void onStatusUpdate(int currentFrame, int totalFrame) {
        if (currentFrame == totalFrame)
            SwingUtilities.invokeLater(() -> {
                settingField.setEnabled(true);
                splitPane1.setBottomComponent(inputFileBar);
                progressPanel = new ProgressPanel(0, 100);
            });
        else
            SwingUtilities.invokeLater(() -> {
                progressPanel.setProgress(FastMath.round((float) currentFrame / totalFrame * 100));
            });
    }
}
