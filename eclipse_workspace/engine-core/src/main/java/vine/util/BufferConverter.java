package vine.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Utility class for converting number array objects into buffer objects.
 * 
 * @author Steffen
 *
 */
public final class BufferConverter {

    private BufferConverter() {
    }

    /**
     * Creates a float buffer object.
     * 
     * @param array
     *            The float array that should be converted into a buffer
     * @return The buffer of the array
     */
    public static FloatBuffer createFloatBuffer(final float[] array) {
        if (array == null) {
            throw new NullPointerException("Can't convert null to an float buffer object");
        }
        final FloatBuffer result = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        result.put(array).flip();
        return result;
    }

    /**
     * Creates a int buffer object.
     * 
     * @param array
     *            The int array that should be converted into a buffer
     * @return The buffer of the array
     */
    public static IntBuffer createIntBuffer(final int[] array) {
        if (array == null) {
            throw new NullPointerException("Can't convert null to an int buffer object");
        }
        final IntBuffer result = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder())
                .asIntBuffer();
        result.put(array).flip();
        return result;
    }

}
