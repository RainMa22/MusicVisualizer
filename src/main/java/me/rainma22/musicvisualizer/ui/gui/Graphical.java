package me.rainma22.musicvisualizer.ui.gui;


import javax.swing.*;
import java.awt.*;
public class Graphical extends JFrame {
    public Graphical(int width, int height){
        super("Music Visualizer");
        setSize(new Dimension(width,height));
        setResizable(false);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(new JTextField("Hello world!"));
        add(new FileBar("test",""));
        add(new FileBar("test2", ""));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    public void start(){
        setVisible(true);
        revalidate();

    }

}
