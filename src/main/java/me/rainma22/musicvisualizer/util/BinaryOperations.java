package me.rainma22.musicvisualizer.util;

public class BinaryOperations {
    public static long nextPowerOfTwo(long in){
        long out = 1;
        for(;out < in; out = out << 1);
        return out;
    }

    public static int nextPowerOfTwo(int in){
        int out = 1;
        for(;out < in; out = out << 1);
        return out;
    }

    public static short unsignedByte(byte in){
        return (short) ((short) in & 0xff);
    }
}
