package me.rainma22.musicvisualizer.ui.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class FileBar extends JPanel {
    private JFileChooser chooser;
    private JTextField pathField;
    private JButton chooserBtn;
    private JLabel label;

    public FileBar(String name, String path) {
//        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setLayout(new BorderLayout());
        chooser = new JFileChooser(path);
        pathField = new JTextField(path);
        chooserBtn = new JButton("File");
        label = new JLabel(name);
        add(label);
        add(pathField);
        add(chooserBtn);
    }

    @Override
    public void repaint() {
        super.repaint();
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        Container parent = getParent();
        if (parent == null) return;
        Dimension selfDimensions = new Dimension(width,height);
        setPreferredSize(selfDimensions);
        int childWidthUnit = selfDimensions.width / 10;
        int childHeightUnit = selfDimensions.height / 10;
        setBorder(new EmptyBorder(childHeightUnit, childWidthUnit / 2, childHeightUnit, childWidthUnit / 2));
        label.setPreferredSize(new Dimension(2 * childWidthUnit, 8 * childHeightUnit));
        pathField.setPreferredSize(new Dimension(5 * childWidthUnit, 8 * childHeightUnit));
        chooserBtn.setPreferredSize(new Dimension(2 * childWidthUnit, 8 * childHeightUnit));

    }
}
