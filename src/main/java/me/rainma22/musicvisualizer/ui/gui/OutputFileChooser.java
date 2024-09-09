package me.rainma22.musicvisualizer.ui.gui;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class OutputFileChooser extends JFileChooser {
    private static final String[] SUPPORTED_FILES_DESCS = new String[]{
            "Portable Network Graphics (.png)",
            "MP4 file (.mp4)",
            "WEBM file (.webm)",
            "MOV file (.mov)",
            "AVI file (.avi)"
    };
    private static final String[] SUPPORTED_FILES = new String[]{
            "png","mp4", "webm", "mov", "avi"
    };
    public OutputFileChooser(File currentDirectory){
        super(currentDirectory);
        for (int i = 0; i < SUPPORTED_FILES.length; i++) {
            FileFilter filter = new FileNameExtensionFilter(SUPPORTED_FILES_DESCS[i], SUPPORTED_FILES[i]);
            if (i == 0)
                setFileFilter(filter);
            else
                addChoosableFileFilter(filter);
        }
    }

    @Override
    public File getSelectedFile() {
        File file = super.getSelectedFile();
        FileNameExtensionFilter filter = (FileNameExtensionFilter)getFileFilter();
        if (filter == null) return file;

        String extension = filter.getExtensions()[0];
        if (!file.getName().toLowerCase().endsWith("."+extension)){
            file = new File(file.getParentFile(), file.getName()+"."+extension);
        }
        return file;
    }
}
