package me.rainma22.musicvisualizer.ui.gui;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileBar extends JPanel {
    private JFileChooser chooser;
    private JPanel leftPadding, rightPadding;
    private JTextField pathField;
    private JButton chooserBtn;
    private JLabel label;

    public FileBar(String name, String path) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        chooser = new JFileChooser(path);
        pathField = new JTextField(path);
        chooserBtn = new JButton("File");
        label = new JLabel(name, SwingConstants.CENTER);
//        label.setBorder(new LineBorder(Color.BLACK, 1));
        leftPadding = new JPanel();
        rightPadding = new JPanel();
        chooserBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = chooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION)
                    pathField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
        add(leftPadding);
        add(label);
        add(pathField);
        add(chooserBtn);
        add(rightPadding);
    }

    public void addFileFilter(FileFilter filter){
        chooser.addChoosableFileFilter(filter);
    }
    public void setFileFilter(FileFilter filter){
        chooser.setFileFilter(filter);
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
        leftPadding.setPreferredSize(new Dimension(childWidthUnit/2, 10*childWidthUnit));
        label.setPreferredSize(new Dimension(childWidthUnit, 8 * childHeightUnit));
        pathField.setPreferredSize(new Dimension(7* childWidthUnit, 8 * childHeightUnit));
        chooserBtn.setPreferredSize(new Dimension(childWidthUnit, 8 * childHeightUnit));
        rightPadding.setPreferredSize(new Dimension(childWidthUnit/2, 10*childWidthUnit));
    }


}
