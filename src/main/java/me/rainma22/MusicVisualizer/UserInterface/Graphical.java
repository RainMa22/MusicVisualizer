package me.rainma22.MusicVisualizer.UserInterface;


import javax.swing.*;
import java.awt.*;

public class Graphical extends JFrame {
    public Graphical(int width, int height){
        super("Music Visualizer");
        setSize(new Dimension(width,height));
        setResizable(false);
        add(new JTextField("Hello world!"));
    }
    public void start(){
        setVisible(true);
        revalidate();
    }

}
