package me.rainma22.MusicVisualizer.ImageProcessor.Blur;

import me.rainma22.MusicVisualizer.Utils.BinaryOperations;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.nio.DoubleBuffer;
import java.util.Arrays;

import static java.lang.Double.NaN;

/**
 * a Gaussian Blur implementation using 2-pass 1D gaussian and FFT
 **/
public class GaussianBlur {
    private final double[] kernel;
    private final FastFourierTransformer transformer;
    private Complex[] transformedKernelX;
    private Complex[] transformedKernelY;
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

    private void filterWidth(int inputWidth, int inputHeight, Raster imgData, WritableRaster outputRaster) {

        int transformSize = BinaryOperations.nextPowerOfTwo(inputWidth + kernel.length);

        if (transformedKernelX == null || transformedKernelX.length != inputWidth) {
            transformedKernelX = transformer.transform(Arrays.copyOf(kernel, transformSize), TransformType.FORWARD);
        }
        DoubleBuffer buffer = DoubleBuffer.allocate(transformSize);
        for (int i = 0; i < inputHeight; i++) {
            for (int colorBand = 0; colorBand < 3; colorBand++) {
                double[] rowData_OneColor = imgData.getSamples(0, i, inputWidth, 1, colorBand, new double[inputWidth]);
//                rowData_OneColor = Arrays.stream(rowData_OneColor).map(x -> FastMath.pow(x, 2)).toArray();
                rowData_OneColor = Arrays.copyOf(rowData_OneColor, transformSize);
                Complex[] transformed_OneColor = transformer.transform(rowData_OneColor, TransformType.FORWARD);
                for (int j = 0; j < transformed_OneColor.length; j++) {
                    transformed_OneColor[j] = transformed_OneColor[j].multiply(transformedKernelX[j]);
                }

                Complex[] outData_oneColor = transformer.transform(transformed_OneColor, TransformType.INVERSE);
                for (int j = 0; j < inputWidth; j++) {
//                    buffer.put(FastMath.sqrt(outData_oneColor[j].getReal()));
                    buffer.put(outData_oneColor[j].getReal());
                }
                buffer.clear();
                outputRaster.setSamples(0, i, inputWidth, 1, colorBand, buffer.array());
            }
        }
    }

    private void filterHeight(int inputWidth, int inputHeight, Raster imgData, WritableRaster outputRaster) {
        int transformSize = BinaryOperations.nextPowerOfTwo(inputHeight + kernel.length);

        if (transformedKernelY == null || transformedKernelY.length != inputHeight) {
            transformedKernelY = transformer.transform(Arrays.copyOf(kernel, transformSize), TransformType.FORWARD);
        }
        DoubleBuffer buffer = DoubleBuffer.allocate(transformSize);
        for (int i = 0; i < inputWidth; i++) {
            for (int colorBand = 0; colorBand < 3; colorBand++) {
                double[] columnData_OneColor = imgData.getSamples(i, 0, 1, inputHeight, colorBand, new double[inputHeight]);
//                columnData_OneColor = Arrays.stream(columnData_OneColor).map(x-> FastMath.pow(x, 2)).toArray();
                columnData_OneColor = Arrays.copyOf(columnData_OneColor, transformSize);
                Complex[] transformed_OneColor = transformer.transform(columnData_OneColor, TransformType.FORWARD);
                for (int j = 0; j < transformed_OneColor.length; j++) {
                    transformed_OneColor[j] = transformed_OneColor[j].multiply(transformedKernelY[j]);
                }

                Complex[] outData_oneColor = transformer.transform(transformed_OneColor, TransformType.INVERSE);
                for (int j = 0; j < inputHeight; j++) {
//                    buffer.put(FastMath.sqrt(outData_oneColor[j].getReal()));
                    buffer.put(outData_oneColor[j].getReal());
                }
                buffer.clear();
                outputRaster.setSamples(i, 0, 1, inputHeight, colorBand, buffer.array());
            }
        }
        return;
    }

    public BufferedImage filter(BufferedImage input, BufferedImage output) {
        if (!active) return input;
        int inputWidth = input.getWidth();
        int inputHeight = input.getHeight();

        Raster imgData = input.getData();

        if (output == null) {
            output = new BufferedImage(inputWidth, inputHeight, input.getType());
        }
        WritableRaster outputRaster = output.getRaster();
        filterWidth(inputWidth, inputHeight, imgData, outputRaster);
        imgData = output.getData();
        filterHeight(inputWidth, inputHeight, imgData, outputRaster);
        return output;
    }

}
