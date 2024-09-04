package me.rainma22.MusicVisualizer.ImageProcessor.Blur;

import me.rainma22.MusicVisualizer.Utils.BinaryOperations;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.apache.commons.math3.util.FastMath;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Double.NaN;

/**
 * a Gaussian Blur implementation using 2-pass 1D gaussian and FFT
 **/
public class GaussianBlur {
    private final double[] kernel;
    private final FastFourierTransformer transformer;
    private ArrayList<Complex[]> transformedKernels;
    private boolean active;

    public GaussianBlur(float sigma) {
        this(sigma, (int) (sigma * 2));

    }

    public GaussianBlur(int size) {
        this(size / 2f, size);
    }

    public GaussianBlur(float sigma, int size) {
        active = true;
        if (size <= 1 || sigma <= 0) active = false;
        else if (size % 2 == 0) size++;
        this.kernel = calculateKernel(sigma, size);
        transformer = new FastFourierTransformer(DftNormalization.STANDARD);
        transformedKernels = new ArrayList<>(2);
        transformedKernels.add(null);
        transformedKernels.add(null);
    }

    /**
     * calculates an 1D Gaussian Blur Kernel which the sum of all the units = 1;
     *
     * @param sigma sigma of the Gaussian Blur
     * @param size  the length of the kernel
     * @return an 1D Gaussian Blur Kernel which the sum of all the units = 1;
     */
    private double[] calculateKernel(float sigma, int size) {
        if (!active) return new double[]{NaN};
        double[] out = new double[size];
        double sum = 0;
        double a = (2 * Math.PI * sigma * sigma);
        for (int i = -size / 2 - 1; i < size / 2; i++) {

            int xPos = i + size / 2 + 1;
            out[xPos] = (Math.exp(-(i * i) / (2 * sigma * sigma)) / a);
            sum += out[xPos];
        }
        for (int i = 0; i < size; i++) {
            out[i] = out[i] / (sum);
        }
        return out;
    }

    private void filterHelper(int inputWidth, int inputHeight, Raster imgData, WritableRaster outputRaster, FilterDirection direction) {

        boolean isHorizontal = direction == FilterDirection.HORIZONTAL;
        int ogSize;
        int otherSize;
        int transformSize;
        int transformedKernelID;

        if (isHorizontal) {
            ogSize = inputWidth;
            otherSize = inputHeight;
            transformSize = BinaryOperations.nextPowerOfTwo(ogSize + kernel.length * 2);
            transformedKernelID = 0;
        } else { //direction == FilterDirection.Vertical
            ogSize = inputHeight;
            otherSize = inputWidth;
            transformSize = BinaryOperations.nextPowerOfTwo(ogSize + kernel.length * 2);
            transformedKernelID = 1;
        }

        Complex[] transformedKernel = transformedKernels.get(transformedKernelID);
        if (transformedKernel == null || transformedKernel.length != ogSize) {
            transformedKernels.set(transformedKernelID,
                    transformer.transform(Arrays.copyOf(kernel, transformSize), TransformType.FORWARD));
            transformedKernel = transformedKernels.get(transformedKernelID);
        }

        DoubleBuffer buffer = DoubleBuffer.allocate(transformSize);
        for (int i = 0; i < otherSize; i++) {
            for (int colorBand = 0; colorBand < 3; colorBand++) {

                double[] Sample_OneColor;
                if (isHorizontal)
                    Sample_OneColor = imgData.getSamples(0, i, ogSize, 1, colorBand, new double[ogSize]);
                else
                    Sample_OneColor = imgData.getSamples(i, 0, 1, ogSize, colorBand, new double[ogSize]);

                Sample_OneColor = Arrays.stream(Sample_OneColor).map(x -> FastMath.pow(x, 2)).toArray();
                Sample_OneColor = Arrays.copyOf(Sample_OneColor, transformSize);
                Complex[] transformed_OneColor = transformer.transform(Sample_OneColor, TransformType.FORWARD);
                for (int j = 0; j < transformed_OneColor.length; j++) {
                    transformed_OneColor[j] = transformed_OneColor[j].multiply(transformedKernel[j]);
                }

                Complex[] outData_oneColor = transformer.transform(transformed_OneColor, TransformType.INVERSE);
                for (int j = 0; j < ogSize; j++) {
                    buffer.put(FastMath.sqrt(outData_oneColor[j].getReal()));
                }
                buffer.clear();

                if (isHorizontal)
                    outputRaster.setSamples(0, i, ogSize, 1, colorBand, buffer.array());
                else
                    outputRaster.setSamples(i, 0, 1, ogSize, colorBand, buffer.array());
            }
        }
    }

    ;

    public BufferedImage filter(BufferedImage input, BufferedImage output) {
        if (!active) return input;
        int inputWidth = input.getWidth();
        int inputHeight = input.getHeight();

        Raster imgData = input.getData();

        if (output == null) {
            output = new BufferedImage(inputWidth, inputHeight, input.getType());
        }
        WritableRaster outputRaster = output.getRaster();
        filterHelper(inputWidth, inputHeight, imgData, outputRaster, FilterDirection.HORIZONTAL);
        imgData = output.getData();
        filterHelper(inputWidth, inputHeight, imgData, outputRaster, FilterDirection.VERTICAL);
        return output;
    }

//    private void filterHeight(int inputWidth, int inputHeight, Raster imgData, WritableRaster outputRaster) {
//        int transformSize = BinaryOperations.nextPowerOfTwo(inputHeight + kernel.length);
//
//        if (transformedKernelY == null || transformedKernelY.length != inputHeight) {
//            transformedKernelY = transformer.transform(Arrays.copyOf(kernel, transformSize), TransformType.FORWARD);
//        }
//        DoubleBuffer buffer = DoubleBuffer.allocate(transformSize);
//        for (int i = 0; i < inputWidth; i++) {
//            for (int colorBand = 0; colorBand < 3; colorBand++) {
//                double[] columnData_OneColor = imgData.getSamples(i, 0, 1, inputHeight, colorBand, new double[inputHeight]);
//                columnData_OneColor = Arrays.stream(columnData_OneColor).map(x-> FastMath.pow(x, 2)).toArray();
//                columnData_OneColor = Arrays.copyOf(columnData_OneColor, transformSize);
//                Complex[] transformed_OneColor = transformer.transform(columnData_OneColor, TransformType.FORWARD);
//                for (int j = 0; j < transformed_OneColor.length; j++) {
//                    transformed_OneColor[j] = transformed_OneColor[j].multiply(transformedKernelY[j]);
//                }
//
//                Complex[] outData_oneColor = transformer.transform(transformed_OneColor, TransformType.INVERSE);
//                for (int j = 0; j < inputHeight; j++) {
//                    buffer.put(FastMath.sqrt(outData_oneColor[j].getReal()));
////                    buffer.put(outData_oneColor[j].getReal());
//                }
//                buffer.clear();
//                outputRaster.setSamples(i, 0, 1, inputHeight, colorBand, buffer.array());
//            }
//        }
//        return;
//    }

    private enum FilterDirection {
        HORIZONTAL,
        VERTICAL
    }

}
