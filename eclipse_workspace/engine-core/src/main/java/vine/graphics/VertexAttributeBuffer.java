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
        this.graphics = GraphicsProvider.getGraphics();
        this.bufferId = this.graphics.generateBuffer();
        this.data = BufferConverter.createFloatBuffer(data);
        this.attribute = attribute;
    }

    public int getPosition() {
        return this.data.position();
    }

    public void append(final float[] values) {
        this.data.put(values, 0, values.length);
    }

    public void resize(final int size) {
        if (VertexAttributeBuffer.LOGGER.isDebugEnabled()) {
            VertexAttributeBuffer.LOGGER
                    .debug("Resized attribute buffer with size " + this.attribute.name() + size);
        }
        this.data = BufferConverter.createFloatBuffer(new float[size]);
    }

    public void reallocate() {
        this.data.flip();
        this.graphics.reallocateVerticeData(this.bufferId, this.data);
        this.data.clear();
    }

    public void bind() {
        this.graphics.bindVertexData(this.bufferId, this.data, this.attribute);
    }

    public VertexAttribute getAttribute() {
        return this.attribute;
    }

}
