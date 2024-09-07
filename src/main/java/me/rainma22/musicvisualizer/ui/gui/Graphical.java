package me.rainma22.musicvisualizer.ui.gui;


import javax.swing.*;
import java.awt.*;
public class Graphical extends JFrame {
    private FileBar inputFileBar, outputFileBar;

    public Graphical(int width, int height){
        super("Music Visualizer");
        JComponent dummy = new JTextField("dummy");
        JPanel dummy2 = new JPanel();
        inputFileBar = new FileBar("Input","");
        outputFileBar = new FileBar("Output", "");

        setSize(new Dimension(width,height));
        setPreferredSize(new Dimension(width,height));
        setMinimumSize(new Dimension(640,360));
        setResizable(true);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        dummy.setPreferredSize(new Dimension(width,height/10*7));
        dummy2.setPreferredSize(new Dimension(width,height/10));
        add(dummy);
        add(inputFileBar);
        add(outputFileBar);
        add(dummy2);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    public void start(){
        setVisible(true);
        revalidate();
    }

}
