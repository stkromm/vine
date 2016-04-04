package vine.graphics;

import java.nio.FloatBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vine.util.BufferConverter;

public class VertexAttributeBuffer {
    private static final Logger LOGGER = LoggerFactory.getLogger(VertexAttributeBuffer.class);
    private final Graphics graphics;
    private final VertexAttribute attribute;
    private final int bufferId;
    private FloatBuffer data;

    public VertexAttributeBuffer(float[] data, VertexAttribute attribute) {
        graphics = GraphicsProvider.getGraphics();
        bufferId = graphics.generateBuffer();
        this.data = BufferConverter.createFloatBuffer(data);
        this.attribute = attribute;
    }

    public int getPosition() {
        return data.position();
    }

    public void append(final float[] values) {
        if (LOGGER.isDebugEnabled() && data.capacity() < values.length) {
            LOGGER.debug("OVERFLOW:\n" + data.capacity() + "\n" + data.position() + "" + values.length + "\n\n");
        } else {
            data.put(values, 0, values.length);
        }
    }

    public void resize(final int size) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Resized attribute buffer with size " + size);
        }
        data = BufferConverter.createFloatBuffer(new float[size]);
    }

    public void reallocate() {
        data.flip();
        graphics.reallocateVerticeData(bufferId, data);
        data.clear();
    }

    public void bind() {
        graphics.bindVertexData(bufferId, data, attribute);
    }

    public VertexAttribute getAttribute() {
        return attribute;
    }
}
