package me.rainma22.musicvisualizer.ui.gui;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Graphical extends JFrame {

    private JSplitPane splitPane,splitPane1;
    private SettingsPanel settingField;
    private PreviewPanel previewPanel;
    private FileBar inputFileBar;
    private JMenuBar menuBar;

    public Graphical(int width, int height){
        super("Music Visualizer");
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem ffmpegPathItem = new JMenuItem("Select Path to FFmpeg");
        fileMenu.add(ffmpegPathItem);
        menuBar.add(fileMenu);

        previewPanel = new PreviewPanel();
        settingField = SettingsPanel.fromSettings();

        inputFileBar = new FileBar("Input","");
        inputFileBar.setFileFilter(new FileNameExtensionFilter("Wave File(.wav)", "wav", "Wav","WAV"));
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane1.setPreferredSize(new Dimension(width,height*9/10));


        setSize(new Dimension(width,height));
        setPreferredSize(new Dimension(width,height));
        setMinimumSize(new Dimension(640,360));
        setResizable(true);


        splitPane.setPreferredSize(new Dimension(width,height/10*9));
        splitPane.setSize(width,height/10*9);
        splitPane.setDividerLocation(.2);

        splitPane.setLeftComponent(settingField);
        splitPane.setRightComponent(previewPanel);
        splitPane1.setTopComponent(splitPane);
        splitPane1.setBottomComponent(inputFileBar);
        setJMenuBar(menuBar);
        setContentPane(splitPane1);
        splitPane1.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                splitPane1.setDividerLocation(.95f);
                splitPane1.removeComponentListener(this);
            }
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    public void start(){
        setVisible(true);
        revalidate();
    }

}
