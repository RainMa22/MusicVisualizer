package me.rainma22.musicvisualizer.settings;

public interface SettingsChangeListener {
    void update(String UpdatedKey, SettingsEntry entry);
}
