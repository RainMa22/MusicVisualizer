package me.rainma22.musicvisualizer.ui.gui;

import me.rainma22.musicvisualizer.settings.SettingsManager;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Collection;

import static me.rainma22.musicvisualizer.util.ui.gui.GuiUtils.panelFromSetting;

public class SettingsPanel extends JPanel {

    private JButton exportBtn;
    private SettingsPanel() {
        super();
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);
    }

    public JButton getExportBtn() {
        return exportBtn;
    }

    public void setExportBtn(JButton exportBtn) {
        this.exportBtn = exportBtn;
    }

    public void onExport(ActionListener actionListener){
        exportBtn.addActionListener(actionListener);
    }
    public static SettingsPanel fromSettings() {
        SettingsManager settings = SettingsManager.getSettingsManager();
        Collection<String> keys = settings.keyCollection();
        SettingsPanel out = new SettingsPanel();
        for (String key : keys) {
            if (key.equals("path_to_ffmpeg")) continue;
            out.add(panelFromSetting(key, settings.getEntry(key)));
        }
        out.setExportBtn(new JButton("Export"));
        out.add(out.getExportBtn());

        return out;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        exportBtn.setEnabled(enabled);
    }
}
