package vine.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * @author Steffen
 *
 */
public class BufferConverter {

    private BufferConverter() {
    }

    /**
     * Creates a float buffer object.
     * 
     * @param array
     *            The float array that should be converted into a buffer
     * @return The buffer of the array
     */
    public static FloatBuffer createFloatBuffer(float[] array) {
        FloatBuffer result = ByteBuffer.allocateDirect((array == null ? 0 : array.length) << 2)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        result.put(array == null ? new float[] {} : array).flip();
        return result;
    }

    /**
     * Creates a int buffer object.
     * 
     * @param array
     *            The int array that should be converted into a buffer
     * @return The buffer of the array
     */
    public static IntBuffer createIntBuffer(int[] array) {
        IntBuffer result = ByteBuffer.allocateDirect((array == null ? 0 : array.length) << 2)
                .order(ByteOrder.nativeOrder()).asIntBuffer();
        result.put(array == null ? new int[] {} : array).flip();
        return result;
    }

}
