package me.rainma22.musicvisualizer.ui.gui;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
public class Graphical extends JFrame {

    private JSplitPane splitPane,splitPane1;
    private SettingsPanel settingField;
    private JComponent dummy;
    private FileBar inputFileBar;

    public Graphical(int width, int height){
        super("Music Visualizer");
        dummy = new JTextField("dummy");
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
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));


        splitPane.setPreferredSize(new Dimension(width,height/10*9));
        splitPane.setSize(width,height/10*9);


        splitPane.setLeftComponent(settingField);
        splitPane.setRightComponent(dummy);
        splitPane1.setTopComponent(splitPane);
        splitPane1.setBottomComponent(inputFileBar);
        splitPane1.setDividerLocation(0.9f);
        add(splitPane1);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    public void start(){
        setVisible(true);
        revalidate();
    }

}
