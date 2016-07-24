package vine.graphics;

import java.nio.FloatBuffer;

import vine.util.BufferConverter;
import vine.util.Log;

public class VertexAttributeBuffer
{
    private final Graphics        graphics;
    private final VertexAttribute attribute;
    private final int             bufferId;
    private FloatBuffer           data;

    public VertexAttributeBuffer(final float[] data, final VertexAttribute attribute)
    {
        graphics = GraphicsProvider.getGraphics();
        bufferId = graphics.generateBuffer();
        this.data = BufferConverter.createFloatBuffer(data);
        this.attribute = attribute;
    }

    public int getPosition()
    {
        return data.position();
    }

    public void append(final float[] values)
    {

        if (data.remaining() < values.length)
        {
            Log.debug(this, "Resized unintentionally");
            Log.debug("Buffer-capacity:" + data.capacity());
            Log.debug("Buffer-position:" + data.position());
            Log.debug("Appended values:" + values.length);
            final float[] copy = new float[data.position()];
            data.clear();
            data.get(copy);
            resize(data.capacity() * 2);
            append(copy);
        }

        data.put(values, 0, values.length);
    }

    public void resize(final int size)
    {
        // Log.lifecycle(this, "Resized attribute buffer with size %s %d",
        // attribute.name(), Integer.valueOf(size));
        final float[] copy = new float[data.position()];
        data.clear();
        data.get(copy);
        data = BufferConverter.createFloatBuffer(new float[size]);
        append(copy);
    }

    public void reallocate()
    {
        data.flip();
        data.rewind();
        graphics.reallocateAttributeData(bufferId, data);
        data.clear();
    }

    public void bind()
    {
        graphics.bindVertexAttribute(bufferId, data, attribute);
    }

    public VertexAttribute getAttribute()
    {
        return attribute;
    }
}
