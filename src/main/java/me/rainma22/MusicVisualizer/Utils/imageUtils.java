package me.rainma22.MusicVisualizer.Utils;

import java.nio.IntBuffer;

public class imageUtils {
    /**
     * returns the unflattened x,y indices of the flattened index
     *
     * @param index      flattened index
     * @param w          width of the list
     * @param h          height of the list
     * @param isRowMajor true if row major ordering otherwise it is column major
     **/
    public static int[] getUnflattenedIndices(int index, int w, int h, boolean isRowMajor) {
        if (isRowMajor)
            return new int[]{index % w, index / w};
        else
            return new int[]{index / h, index % h};
    }

    /**
     * returns the flattened index of the unflattened x,y indices
     *
     * @param x          unflattened index x
     * @param y          unflattened index y
     * @param w          width of the list
     * @param h          height of the list
     * @param isRowMajor true if row major ordering otherwise it is column major
     **/
    public static int getFlattenedIndices(int x, int y, int w, int h, boolean isRowMajor) {
        if (isRowMajor)
            return y * w + x;
        else
            return x * h + y;
    }


    private static float[][] calculateKernel(float sigma, int size) {
        float[][] out = new float[size][size];
        float sum = 0;
        float a = (float) (2 * Math.PI * sigma * sigma);
        for (int i = -size / 2 - 1; i < size / 2; i++) {
            for (int j = -size / 2 - 1; j < size / 2; j++) {
                int xPos = i + size / 2 + 1;
                int yPos = j + size / 2 + 1;
                out[xPos][yPos] = (float) (Math.exp(-(i * i + j * j) / (2 * sigma * sigma)) / a);
                sum += out[xPos][yPos];
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                out[i][j] /= sum;
            }
        }
        return out;
    }

    private static int retrieve_nearby_sums(int[] srcColor, int x, int y, int w, int h, float[][] kernel, boolean isRowMajor) {
        int sum = 0;
        int size = kernel.length;
        for (int i = x - size / 2 - 1; i < x + size / 2; i++) {
            for (int j = y - size / 2; j < y + size / 2; j++) {
                int xPos = i - x + size / 2 + 1;
                int yPos = j - y + size / 2 + 1;
                int pixRGB = retrieve_safe(srcColor, i, j, w, h, isRowMajor);
                int pixVal = multiplyRGB(pixRGB, kernel[xPos][yPos]);
                sum = addRGB(sum, pixVal);
            }
        }
        return sum;
    }

    private static int retrieve_safe(int[] array, int x, int y, int w, int h, boolean isRowMajor) {
        x = Math.abs(x) % w;
        y = Math.abs(y) % h;
        int index = getFlattenedIndices(x, y, w, h, isRowMajor);
        return array[index];
    }

    private static int[] unpackRGB(int val) {
        return new int[]{(val >> 16) & 0xFF, (val >> 8) & 0xFF, val & 0xFF};
    }


    private static int packRGB(int[] rgb) {
        return ((rgb[0] & 0xFF) << 16) + ((rgb[1] & 0xFF) << 8) + (rgb[2] & 0xFF);
    }

    private static int multiplyRGB(int rgbA, float val) {
        int[] a = unpackRGB(rgbA);
        for (int i = 0; i < 3; i++) {
            a[i]*=a[i];
            a[i] = (int) (a[i] * val);
            a[i] = (int) Math.sqrt(a[i]);
        }
        return packRGB(a);
    }

    private static int addRGB(int rgbA, int rgbB) {
        int[] a = unpackRGB(rgbA);
        int[] b = unpackRGB(rgbB);

        for (int i = 0; i < 3; i++) {
            a[i]*=a[i];
            b[i]*=b[i];

            a[i] += b[i];
            a[i] = (int) Math.sqrt(a[i]);
        }
        return packRGB(a);
    }

    /**
     * Gaussian blur
     *
     * @param srcRGB     flattened array of rgb ints of any format
     * @param w          width of the source image
     * @param h          height of the source image
     * @param kernelSize the size of the guassian blur kernel
     **/
    public static int[] GuassianBlur(int[] srcRGB, int w, int h, int kernelSize) {
        if (kernelSize <= 1) return srcRGB;
        float sigma = kernelSize / 2f;
//        sigma = 100000;
        IntBuffer buffer = IntBuffer.allocate(w * h);
        for (int i = 0; i < srcRGB.length; i += 3) {
            buffer.put(packRGB(new int[]{srcRGB[i], srcRGB[i + 1], srcRGB[i + 2]}));
        }
        int[] pixels = new int[w * h];
        buffer.clear();
        buffer.get(pixels, 0, w * h);
        float[][] kernel = calculateKernel(sigma, kernelSize);

        int[] outVal = new int[srcRGB.length];

        buffer.clear();
        for (int i = 0; i < w * h; i++) {
            int[] coords = getUnflattenedIndices(i, w, h, true);
            int x = coords[0];
            int y = coords[1];
            int sum = retrieve_nearby_sums(pixels, x, y, w, h, kernel, true);
            buffer.put(sum);
        }
        for (int i = 0; i < srcRGB.length; i += 3) {
            outVal[i] = (buffer.get(i / 3) >> 16) & 0xFF;
            outVal[i + 1] = (buffer.get(i / 3) >> 8) & 0xFF;
            outVal[i + 2] = buffer.get(i / 3) & 0xFF;
        }
        return outVal;
    }

}
