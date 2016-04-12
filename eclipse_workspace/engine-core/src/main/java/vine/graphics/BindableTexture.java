package vine.graphics;

public interface BindableTexture {
    void bind();

    void unbind();

    int getWidth();

    int getHeight();
}
