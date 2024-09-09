package me.rainma22.musicvisualizer.ui.gui;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ProgressPanel extends JPanel {
    private JProgressBar progressBar;
    public ProgressPanel(int min, int max){
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        progressBar = new JProgressBar(JProgressBar.HORIZONTAL, min,max);
        add(progressBar);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                progressBar.setBounds(0,0,getWidth(),getHeight());
            }
        });
    }
    public void setProgress(int progress){
        progressBar.setValue(progress);
    }

}
