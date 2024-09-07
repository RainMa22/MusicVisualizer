package me.rainma22.musicvisualizer.settings;

public class SettingsEntry {
    private String value;
    private EntryType type;
    private String[] possibleValues;
    private String description;

    public SettingsEntry(String value, EntryType type, String[] possibleValues, String description) {
        assert type != EntryType.FACTOR || possibleValues.length != 0;
        setValue(value);
        this.type = type;
        this.possibleValues = (possibleValues == null) ? null : possibleValues.clone();
        this.description = description;
    }

    public static SettingsEntry newStringEntry(String value, String description) {
        return new SettingsEntry(value, EntryType.STRING, null, description);
    }

    public static SettingsEntry newIntEntry(String value, String description) {
        return new SettingsEntry(value, EntryType.INT, null, description);
    }


    public static SettingsEntry newDoubleEntry(String value, String description) {
        return new SettingsEntry(value, EntryType.DOUBLE, null, description);
    }

    public static SettingsEntry newColorEntry(String value, String description) {
        return new SettingsEntry(value, EntryType.COLOR, null, description);
    }
    public static SettingsEntry newFileEntry(String value, String description) {
        return new SettingsEntry(value, EntryType.FILE, null, description);
    }
    public static SettingsEntry newFolderEntry(String value, String description) {
        return new SettingsEntry(value, EntryType.FOLDER, null, description);
    }

    @Override
    public String toString() {
        return value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public EntryType getType() {
        return type;
    }

    public String[] getPossibleValues() {
        return possibleValues;
    }

    public String getDescription() {
        return description;
    }
}
