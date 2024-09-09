package me.rainma22.musicvisualizer.exporter;

public interface ExportStatusListener {
    void onStatusUpdate(int currentFrame, int totalFrame);
}
