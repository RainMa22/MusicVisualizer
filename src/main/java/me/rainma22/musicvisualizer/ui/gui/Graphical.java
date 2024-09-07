package me.rainma22.musicvisualizer.ui.gui;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
public class Graphical extends JFrame {

    private JComponent settingField;
    private FileBar inputFileBar;

    public Graphical(int width, int height){
        super("Music Visualizer");
//        settingField = new JTextField("dummy");
        settingField = SettingsPanel.fromSettings();
        JPanel dummy2 = new JPanel();
        inputFileBar = new FileBar("Input","");
        inputFileBar.setFileFilter(new FileNameExtensionFilter("Wave File(.wav)", "wav", "Wav","WAV"));

        setSize(new Dimension(width,height));
        setPreferredSize(new Dimension(width,height));
        setMinimumSize(new Dimension(640,360));
        setResizable(true);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        settingField.setPreferredSize(new Dimension(width,height/10*8));
        dummy2.setPreferredSize(new Dimension(width,height/10));
        add(settingField);
        add(inputFileBar);
        add(dummy2);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    public void start(){
        setVisible(true);
        revalidate();
    }

}
