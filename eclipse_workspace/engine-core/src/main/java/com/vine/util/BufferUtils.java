package com.vine.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class BufferUtils {

    private BufferUtils() {
    }

    /**
     * Creates a byte buffer object.
     */
    public static ByteBuffer createByteBuffer(byte[] array) {
        ByteBuffer result = ByteBuffer.allocateDirect(array.length).order(ByteOrder.nativeOrder());
        result.put(array).flip();
        return result;
    }

    /**
     * Creates a float buffer object.
     */
    public static FloatBuffer createFloatBuffer(float[] array) {
        FloatBuffer result = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        result.put(array).flip();
        return result;
    }

    /**
     * Creates a int buffer object.
     */
    public static IntBuffer createIntBuffer(int[] array) {
        IntBuffer result = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder()).asIntBuffer();
        result.put(array).flip();
        return result;
    }

}
