package vine.graphics;

import org.lwjgl.opengl.GL11;

public interface Graphics {

    void makeContext(long context);

    void init();

    void setViewport(int x, int y, int width, int height);

    void clearBuffer();

    void swapBuffers();

}