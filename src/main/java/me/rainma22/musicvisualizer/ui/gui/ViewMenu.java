package me.rainma22.musicvisualizer.ui.gui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class ViewMenu extends JMenu {
    private JMenu themes;
    private ButtonGroup themeButtonGroup;
    private Graphical graphical;
    private LookAndFeel[] lafs;

    public ViewMenu(Graphical graphical) {
        super("View");
        lafs = new LookAndFeel[]{
                new FlatLightLaf(),
                new FlatDarkLaf(),
        };
        this.graphical = graphical;
        themes = new JMenu("Themes");
        themeButtonGroup = new ButtonGroup();

        for (int i = 0; i < lafs.length; i++) {
            LookAndFeel laf = lafs[i];
            JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(laf.getName());
            if (i == 0) {
                menuItem.setSelected(true);
            }
            menuItem.addActionListener((e) -> {
                graphical.setLookAndFeel(laf);
            });
            themeButtonGroup.add(menuItem);
            themes.add(menuItem);
        }

        add(themes);

    }

}
