package me.rainma22.MusicVisualizer.Utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BinaryOperationsTests {
    @Test
    public void testInt(){
        int target = 1;
        for (int i = 1; i < (1<<10 + 1); i++) {
            if (target < i) target = target << 1;
            assertEquals(target,BinaryOperations.nextPowerOfTwo(i));
        }
    }

    @Test
    public void testLong(){
        long target = 1;
        for (long i = 1; i < (1<<10 + 1); i++) {
            if (target < i) target = target << 1L;
            assertEquals(target,BinaryOperations.nextPowerOfTwo(i));
        }
    }
}
