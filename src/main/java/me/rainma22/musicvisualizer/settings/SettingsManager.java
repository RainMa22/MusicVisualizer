package me.rainma22.musicvisualizer.settings;

import me.rainma22.intermediateimage.Resources.Image;
import org.apache.commons.math3.util.FastMath;

import java.awt.*;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

/**
 * Singleton Class that manages settings specific to Main
 **/
public class SettingsManager  {
    private static SettingsManager manager = null;
    public static final String KEY_FPS = "fps";
    public static final String KEY_PATH_TO_FFMPEG = "path_to_ffmpeg";
    public static final String KEY_FOREGROUND_IMG = "foreground_img";
    public static final String KEY_BACKGROUND_IMG = "background_img";
    public static final String KEY_ROTATION_THETA = "rotation_per_theta";
    public static final String KEY_LINE_COLOR_HEX = "line_color_hex";
    public static final String KEY_AMPLITUDE_THRESHOLD = "amplitude_threshold";
    public static final String KEY_BLUR_SIZE = "blur_size";
    public static final String KEY_WIDTH = "width";
    public static final String KEY_HEIGHT = "height";
    private List<String> keys;
    private List<SettingsEntry> values;
    private List<SettingsChangeListener> listeners;
    public static SettingsManager getSettingsManager(){
        if (manager == null){
            manager = new SettingsManager();
        }
        return manager;
    }

    private SettingsManager(){
        super();
        keys = new ArrayList<>();
        values = new ArrayList<>();
        listeners = new ArrayList<>(1);
        put(KEY_FPS, SettingsEntry.newIntEntry("60",5,120,"Frames Per Second of Output"));
        put(KEY_PATH_TO_FFMPEG, SettingsEntry.newExecutableEntry("ffmpeg", "Path to FFmpeg Executable"));
        put(KEY_FOREGROUND_IMG, SettingsEntry.newImageEntry("defaults/foregroundIMG.jpg", "Path to Foreground Image"));
        put(KEY_BACKGROUND_IMG, SettingsEntry.newImageEntry("defaults/bg.jpg",  "Path to Background Image"));
        put(KEY_ROTATION_THETA, SettingsEntry.newDoubleEntry("0.0",0, FastMath.PI,"Theta Rotation per Frame"));
        put(KEY_LINE_COLOR_HEX, SettingsEntry.newColorEntry("0x0000ee", "Color of the Visualization Line"));
        put(KEY_AMPLITUDE_THRESHOLD, SettingsEntry.newDoubleEntry("0",0,2, "Amplitude Threshold"));
        put(KEY_BLUR_SIZE, SettingsEntry.newIntEntry("50",1,500, "Blur Size"));
        put(KEY_WIDTH, SettingsEntry.newIntEntry("1920",360,5000, "Output Width"));
        put(KEY_HEIGHT, SettingsEntry.newIntEntry("1080",360,5000, "Output Height"));
    }

    public String getSettingsString() {
        StringJoiner joiner = new StringJoiner(" ");
        for (int i = 0; i < keys.size(); i++) {
            joiner.add(String.format("[%s=%s]", keys.get(i), values.get(i)));
        }
        return joiner.toString();
    }

    public void addListener(SettingsChangeListener listener){
        if (listeners.contains(listener)) return;
        listeners.add(listener);
    }

    public void put(String key,String value){
        assert containsKey(key);
        SettingsEntry entry = values.get(keys.indexOf(key));
        assert entry.getType() != EntryType.FACTOR || Arrays.asList(entry.getPossibleValues()).contains(value);
        entry.setValue(value);
    }

    private void put(String key, SettingsEntry entry){
        if (!keys.contains(key)){
            keys.add(key);
            values.add(entry);
        }else {
            values.set(keys.indexOf(key), entry);
        }
    }


    public void notifyUpdate(String key){
        listeners.forEach(x -> x.update(key,getEntry(key)));

    }

    public String get(String key){
        return values.get(keys.indexOf(key)).toString();
    }

    public SettingsEntry getEntry(String key){
        return values.get(keys.indexOf(key));
    }

    public Collection<String> keyCollection(){
        return Collections.unmodifiableCollection(keys);
    }

    public boolean containsKey(String key){
        return keys.contains(key);
    }

    public Object getObj(String key){
        SettingsEntry entry = getEntry(key);
        switch (entry.getType()){
            case EXECUTABLE:
            case FOLDER:
            case COLOR:
            case STRING:
            case FACTOR:
            default:
                return get(key);
            case DOUBLE:
                return (Double) Double.parseDouble(get(key));
            case INT:
                return (Integer) Integer.parseInt(get(key));
            case IMAGE:
                return new Image(Path.of(get(key)));
        }
    }


    public double getDouble(String key, double defaultVal){
        double val;
        try {
            val = Double.parseDouble(get(key));
        } catch (NumberFormatException nfe){
            val = defaultVal;
            System.err.printf("%s \"%s\" is not a valid number, defaulting to %f \n",
                    key, get(key), defaultVal);
            put(key, String.valueOf(defaultVal));
        }
        return val;
    }

    public Image getImage(String key, Image defaultVal){
        return (keys.contains(key)) ? new Image(Path.of(get(key))): defaultVal;
    }

    public int getInt (String key, int defaultVal){
        int val;
        try {
            val = Integer.parseInt(get(key));
        } catch (NumberFormatException nfe){
            val = defaultVal;
            System.err.printf("%s \"%s\" is not a valid int, defaulting to %d \n",
                    key, get(key), defaultVal);
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
