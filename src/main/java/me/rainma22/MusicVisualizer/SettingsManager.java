package me.rainma22.MusicVisualizer;

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
        put("width", "1920");
        put("height", "1080");
    }

    public String getSettingsString(){
        StringJoiner joiner = new StringJoiner(" ");
        forEach((key, value) -> joiner.add(String.format("[%s = %s]", key, value)));
        return joiner.toString();
    }

    public double getDouble(String key, double defaultVal){
        double val;
        try {
            val = Double.parseDouble(get(key));
        } catch (NumberFormatException nfe){
            val = defaultVal;
            System.err.printf("%s \"%s\" is not a valid number, defaulting to %f \n", key, get("fps"), defaultVal);
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
            System.err.printf("%s \"%s\" is not a valid number, defaulting to %d \n", key, get("fps"), defaultVal);
            put(key, String.valueOf(defaultVal));
        }
        return val;
    }


}
