package me.rainma22.musicvisualizer.ui;

import me.rainma22.musicvisualizer.settings.SettingsManager;
import me.rainma22.musicvisualizer.ui.cli.CommandLine;
import me.rainma22.musicvisualizer.ui.gui.Graphical;
import me.rainma22.musicvisualizer.util.ProcessUtils;

public class Main {
    private static int chunkSize;
    private static boolean ffmpegEnabled = false;
    private static SettingsManager settings = SettingsManager.getSettingsManager();


    public static void main(String[] args) {
        boolean isGUI = true;
        String filePath = "";
        if (args.length > 0) {
            isGUI = !args[0].equalsIgnoreCase("CLI");
        }
        //Check if enough args are used
        if (args.length < 2 && !isGUI) {
            System.out.println("Usage: java -jar MusicVisualizer (CLI|GUI) (filename) " +
                    settings.getSettingsString());
            return;
        } else if (args.length >= 2) {
            filePath = args[1];
        }
        // Parse Settings
        for (int i = 2; i < args.length; i++) {
            String[] keyValPair = args[i].split("=");
            if (keyValPair.length < 2) {
                System.err.printf("Argument \"%s\" does not have a equal sign, ignoring! \n", args[i]);
            } else if (keyValPair.length > 2) {
                System.err.printf("Argument \"%s\" has too many equal signs, using \"%s=%s\"!\n", args[i],
                        keyValPair[0], keyValPair[1]);
            }

            if (!settings.containsKey(keyValPair[0])) {
                System.err.printf("Unknown key %s! Ignoring\n", keyValPair[0]);
            } else {
                settings.put(keyValPair[0], keyValPair[1]);
            }
        }


        String ffmpegPath = settings.get("path_to_ffmpeg");
        if (!(ffmpegEnabled = ProcessUtils.isProgramRunnable(ffmpegPath))) {
            System.err.println("FFmpeg not usable! Using Pure Java instead(Very Slow and no Audio in video output)!");
        }
        if (!isGUI)
            new CommandLine(filePath, ffmpegEnabled)
                    .start();
        else
            new Graphical(1600, 900).start();
        return;
    }
}