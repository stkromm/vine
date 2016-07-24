package vine.gameplay;

import vine.game.scene.Component;
import vine.graphics.Sprite;

public class SpriteComponent extends Component
{
    Sprite sprite;

    public SpriteComponent(final Sprite sprite)
    {
        this.sprite = sprite;
    }

    public Sprite getSprite()
    {
        return sprite;
    }

    @Override
    public void onUpdate(final float delta)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAttach()
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void onDetach()
    {
        super.detach();
    }

    @Override
    public void onDeactivation()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onActivation()
    {
        // TODO Auto-generated method stub

    }

}
