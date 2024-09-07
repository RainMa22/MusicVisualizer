package me.rainma22.musicvisualizer.settings;

import me.rainma22.musicvisualizer.util.MusicUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Set;
import java.util.StringJoiner;

/**
 * Singleton Class that manages settings specific to Main
 **/
public class SettingsManager  {
    private static SettingsManager manager = null;
    private Hashtable<String,SettingsEntry> table;
    public static SettingsManager getSettingsManager(){
        if (manager == null){
            manager = new SettingsManager();
        }
        return manager;
    }

    private SettingsManager(){
        super();
        table = new Hashtable<>();
        put("fps", SettingsEntry.newIntEntry("60",12,60,"Frames Per Second of Output"));
        put("path_to_ffmpeg", SettingsEntry.newFileEntry("ffmpeg", "Path to FFMpeg.exe"));
        put("output_format", new SettingsEntry("MOV",EntryType.FACTOR,
                MusicUtils.SUPPORTED_OUTPUTS,"The Output Extension"));
        put("output_path", SettingsEntry.newFolderEntry("./output",  "Output Folder of File"));
        put("out_file_name", SettingsEntry.newStringEntry("output",  "Output File Name"));
        put("foreground_img", SettingsEntry.newFileEntry("defaults/foregroundIMG.jpg", "Path to Foreground Image"));
        put("background_img", SettingsEntry.newFileEntry("defaults/bg.jpg",  "Path to Background Image"));
        put("rotation_per_theta", SettingsEntry.newDoubleEntry("0.0",0,4,"Theta Rotation per Frame"));
        put("line_color_hex", SettingsEntry.newColorEntry("0x0000ee", "Color of the Visualization Line"));
        put("amplitude_threshold", SettingsEntry.newDoubleEntry("0",0,5, "Amplitude Threshold"));
        put("blur_size", SettingsEntry.newIntEntry("50",1,500, "Blur Size"));
        put("width", SettingsEntry.newIntEntry("1920",360,5000, "Output Width"));
        put("height", SettingsEntry.newIntEntry("1080",360,5000, "Output Height"));
    }

    public String getSettingsString() {
        StringJoiner joiner = new StringJoiner(" ");
        table.forEach((key, value) -> joiner.add(String.format("[%s=%s]", key, value)));
        return joiner.toString();
    }

    public void put(String key,String value){
        assert table.containsKey(key);
        SettingsEntry entry = table.get(key);
        assert entry.getType() != EntryType.FACTOR || Arrays.asList(entry.getPossibleValues()).contains(value);
        entry.setValue(value);
    }

    private void put(String key, SettingsEntry entry){
        table.put(key, entry);
    }

    public String get(String key){
        return table.get(key).toString();
    }

    public SettingsEntry getEntry(String key){
        return table.get(key);
    }

    public Set<String> keySet(){
        return table.keySet();
    }

//    public String get(String key, String defaultVal){
//        return table.get(key).toString();
//    }

    public boolean containsKey(String key){
        return table.containsKey(key);
    }

    public double getDouble(String key, double defaultVal){
        double val;
        try {
            val = Double.parseDouble(get(key));
        } catch (NumberFormatException nfe){
            val = defaultVal;
            System.err.printf("%s \"%s\" is not a valid number, defaulting to %f \n", key, get(key), defaultVal);
            put(key, String.valueOf(defaultVal));
        }
        return val;
    }

    public BufferedImage getImage(String key, BufferedImage defaultVal){
        BufferedImage val;
        try {
            val = ImageIO.read(new File(get(key)));
        } catch (IOException exception){
            System.err.printf("Unable to read \"%s\" as %s, defaulting to %s \n", get(key), key,
                    String.valueOf(defaultVal));
            val = defaultVal;
            put(key, String.valueOf(defaultVal));
        }
        return val;
    }

    public int getInt (String key, int defaultVal){
        int val;
        try {
            val = Integer.parseInt(get(key));
        } catch (NumberFormatException nfe){
            val = defaultVal;
            System.err.printf("%s \"%s\" is not a valid int, defaulting to %d \n", key, get(key), defaultVal);
            put(key, String.valueOf(defaultVal));
        }
        return val;
    }

    public Color getColor (String key, Color defaultVal){
        Color val;
        try {
            val = Color.decode(get(key));
        } catch (NumberFormatException nfe){
            val = defaultVal;
            System.err.printf("%s \"%s\" is not a valid Color, defaulting to %s \n", key, get(key), defaultVal);
            put(key, String.valueOf(defaultVal));
        }
        return val;
    }


}
