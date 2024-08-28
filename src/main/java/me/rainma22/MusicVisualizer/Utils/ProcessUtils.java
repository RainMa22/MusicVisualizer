package me.rainma22.MusicVisualizer.Utils;

import java.util.concurrent.TimeUnit;

/**
 * Utility class for processes**/
public class ProcessUtils {

    /**
     * Check if the process is runnable by attempting to run**/
    public static boolean isProgramRunnable(String appPath){
        try {
            ProcessBuilder pb = new ProcessBuilder(appPath);
            Process p = pb.start();
            p.waitFor(2, TimeUnit.SECONDS);
            if (p.isAlive())
                p.destroyForcibly();
            return true;
        } catch (Exception exception){
            exception.printStackTrace();
            return false;
        }
    }
}
