package me.rainma22.musicvisualizer.util;

import me.rainma22.intermediateimage.Circle;
import me.rainma22.intermediateimage.ColorRGBA;
import me.rainma22.intermediateimage.Effects.ContainerEffects.BackgroundImage;
import me.rainma22.intermediateimage.Effects.PolylineEffects.TransformByAudio;
import me.rainma22.intermediateimage.IntermediateImage;
import me.rainma22.intermediateimage.PolyLine;
import me.rainma22.intermediateimage.Resources.Image;
import me.rainma22.musicvisualizer.settings.SettingsManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.DoubleBuffer;

public class ImageUtils {

    public static double[] addPaddingReflect(double[] data, int leftPaddingLength, int rightPaddingLength) {
        DoubleBuffer newDataBuffer = DoubleBuffer.allocate(data.length + leftPaddingLength + rightPaddingLength);
        for (int i = leftPaddingLength - 1 ; i > -1 ; i--) {
            newDataBuffer.put(data[i]);
        }
        newDataBuffer.put(data);
        for (int i = data.length - 1; i > data.length - rightPaddingLength - 1; i--) {
            newDataBuffer.put(data[i]);
        }
        return newDataBuffer.array();
    }

    public static BufferedImage loadImage(Image resource){
        try {
            return ImageIO.read(resource.getPath().toFile());
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static IntermediateImage getDefaultIntermediateImage(int width,
                                                                int height,
                                                                int sampleSize,
                                                                ColorRGBA strokeColor){
//        SettingsManager settings = SettingsManager.getSettingsManager();
        IntermediateImage result = new IntermediateImage(width, height);
//        ResourceManager resourceManager = result.getResourceManager();
//        result.setBackgroundColorProvider(ColorProvider.ofImage((Image)
//                settings.getObj(SettingsManager.KEY_BACKGROUND_IMG)));
        result.addEffect(new BackgroundImage(result, SettingsManager.KEY_BACKGROUND_IMG));
        int circleDiameter = Math.max(width/5, height/5);
        int circleX = (width - circleDiameter)/2;
        int circleY = (height - circleDiameter)/2;
        Circle center = new Circle(circleX, circleY, circleDiameter/2);
        PolyLine visualization = center.toPolyline(sampleSize);
        center.addEffect(new BackgroundImage(center,SettingsManager.KEY_FOREGROUND_IMG));
        visualization.addEffect(new TransformByAudio(visualization, "Audio", "tbd", "tbd"));
        visualization.setStrokeColor_rgba(strokeColor);
        visualization.setStrokeSize_px(1);
        result.add(center);
        result.add(visualization);
        return result;

    }


}
