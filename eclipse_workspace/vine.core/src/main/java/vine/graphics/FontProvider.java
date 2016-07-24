package vine.graphics;

public interface FontProvider
{
    float getStringWidth(String string);

    float getCharWidth(char c);

    float getCharHeight(char c);
}
