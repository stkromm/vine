package vine.graphics.renderer;

import vine.game.screen.Screen;

public interface Renderer
{
    void prepare(Screen screen);

    void render();

    void finish();

}
