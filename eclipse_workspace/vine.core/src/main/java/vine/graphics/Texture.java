package vine.graphics;

public interface Texture
{
    void bind();

    void unbind();

    int getWidth();

    int getHeight();

    public static enum WrapMode
    {
        CLAMP, REPEAT;
    }

    public enum TextureFilter
    {
        NEAREST, LINEAR, LINEAR_LINEAR, NEAREST_NEAREST, LINEAR_NEAREST, NEAREST_LINEAR;
    }
}
