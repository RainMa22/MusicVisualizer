package me.rainma22.MusicVisualizer.FrameOutput;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * A frame output using FFMPEG**/
public class FrameFFMPEGOutput implements FrameOutput{
    private boolean finished;
    private String pathToFFMPEG;
    private File folder;
    private OutputStream pipeIntoFFMPEG;
    private Process videoProcess;
    private ByteBuffer imgData;
    /**
     * Constructs a FrameFFMPEGOutput, assumes the FFMPEG exists on the given pathToFFMpeg
     * @param folderPath the path to the output folder
     * @param fileNameWithExtension the name of the file, with extension
     * @param pathToAudio the path to the audio File
     * @param pathToFFMPEG the path to the ffmpeg executable
     * @param width the width of the output video
     * @param height the height of the output video
     * @param fps the fps of the output video
     * **/
    public FrameFFMPEGOutput(String folderPath,String fileNameWithExtension,String pathToAudio,String pathToFFMPEG, int width, int height,
                             double fps) throws IOException {
        finished = false;
        folder = new File(folderPath);
        folder.mkdirs();
        this.pathToFFMPEG = pathToFFMPEG;
        ProcessBuilder builder = new ProcessBuilder(this.pathToFFMPEG,
                "-f", "rawvideo",
                "-pix_fmt", "rgb24",
                "-video_size", String.format("%dx%d", width, height),
                "-r", String.valueOf(fps),
                "-i", "pipe:0",
                "-i", new File(pathToAudio).getAbsolutePath(),
                new File(folder,fileNameWithExtension).getAbsolutePath());
//        builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        builder.redirectError(ProcessBuilder.Redirect.INHERIT);
//        builder.redirectError(ProcessBuilder.Redirect.DISCARD);
        videoProcess = builder.start();
        pipeIntoFFMPEG = videoProcess.getOutputStream();
    }

    @Override
    public void writeImage(BufferedImage image, int index) throws IOException {
        assert !finished;
        assert image.getType() == BufferedImage.TYPE_INT_RGB;

        int[] rgb = image.getData().getPixels(0,0,image.getWidth(),image.getHeight(), (int[]) null);

        if (imgData == null) imgData = ByteBuffer.allocate(rgb.length);
        Arrays.stream(rgb).forEachOrdered((int x) -> imgData.put((byte) x));

        pipeIntoFFMPEG.write(imgData.array());
        imgData.clear();
    }

    @Override
    public void finish() throws IOException {
        FrameOutput.super.finish();
        pipeIntoFFMPEG.flush();
        finished = true;
        pipeIntoFFMPEG.close();
        try {
            videoProcess.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
