package me.rainma22.MusicVisualizer.Utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProcessUtilsTest {

    @Test
    public void testProcessExists(){
        assertTrue(ProcessUtils.isProgramRunnable("java"));
    }


    @Test
    public void testProcessNotExists(){
        assertFalse(ProcessUtils.isProgramRunnable("asodiapwriowqrjhqorhqljrhwk"));
    }
}
