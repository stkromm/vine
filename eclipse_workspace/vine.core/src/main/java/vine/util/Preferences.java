package vine.util;

public interface Preferences
{
    public boolean putBoolean(String key, boolean value);

    public boolean putInteger(String key, int value);

    public boolean putLong(String key, long value);

    public boolean putFloat(String key, float value);

    public boolean putString(String key, String value);

    public int getInteger(String key, int value);

    public long getLong(String key, long value);

    public float getFloat(String key, float value);

    public String getString(String key, String value);

    public boolean getBoolean(String key, boolean value);

    public boolean contains(String key);

    public void clear();

    public void remove(String key);
}
