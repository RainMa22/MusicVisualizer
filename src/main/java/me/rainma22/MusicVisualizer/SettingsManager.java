package me.rainma22.MusicVisualizer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.StringJoiner;

/**
 * Singleton Class that manages settings specific to Main
 **/
class SettingsManager extends Hashtable<String,String> {
    private static SettingsManager manager = null;

    public static SettingsManager getSettingsManager(){
        if (manager == null){
            manager = new SettingsManager();
        }
        return manager;
    }

    private SettingsManager(){
        super();
        put("fps", "60");
        put("path_to_ffmpeg", "ffmpeg");
        put("output_format", "MOV");
        put("output_path", "./output");
        put("out_file_name", "output");
        put("foreground_img", "defaults/foregroundIMG.jpg");
        put("background_img", "defaults/bg.jpg");
        put("rotation_per_theta", "0.0");
        put("line_color_hex", "0x0000ee");
        put("amplitude_threshold", "0");
        put("blur_size", "10");
        put("width", "1920");
        put("height", "1080");
    }

    public String getSettingsString(){
        StringJoiner joiner = new StringJoiner(" ");
        forEach((key, value) -> joiner.add(String.format("[%s=%s]", key, value)));
        return joiner.toString();
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
