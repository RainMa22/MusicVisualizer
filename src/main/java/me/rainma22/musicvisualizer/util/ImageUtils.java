package me.rainma22.musicvisualizer.util;

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


}
