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
        this.graphics = GraphicsProvider.getGraphics();
        this.bufferId = this.graphics.generateBuffer();
        this.data = BufferConverter.createFloatBuffer(data);
        this.attribute = attribute;
    }

    public int getPosition()
    {
        return this.data.position();
    }

    public void append(final float[] values)
    {

        if (this.data.remaining() < values.length)
        {
            Log.debug(this, "Resized unintentionally");
            Log.debug("Buffer-capacity:" + this.data.capacity());
            Log.debug("Buffer-position:" + this.data.position());
            Log.debug("Appended values:" + values.length);
            final float[] copy = new float[this.data.position()];
            this.data.clear();
            this.data.get(copy);
            this.resize(this.data.capacity() * 2);
            this.append(copy);
        }

        this.data.put(values, 0, values.length);
    }

    public void resize(final int size)
    {
        if (Log.isLifecycleEnabled())
        {
            Log.lifecycle("Resized attribute buffer with size " + this.attribute.name() + size);
        }
        final float[] copy = new float[this.data.position()];
        this.data.clear();
        this.data.get(copy);
        this.data = BufferConverter.createFloatBuffer(new float[size]);
        this.append(copy);
    }

    public void reallocate()
    {
        this.data.flip();
        this.data.rewind();
        this.graphics.reallocateAttributeData(this.bufferId, this.data);
        this.data.clear();
    }

    public void bind()
    {
        this.graphics.bindVertexAttribute(this.bufferId, this.data, this.attribute);
    }

    public VertexAttribute getAttribute()
    {
        return this.attribute;
    }
}
