package vine.event;

public class KeyEvent extends Event {
    int key;
    int scancode;
    int action;
    int mods;

    public KeyEvent(int key, int scancode, int action, int mods) {
        super();
        this.key = key;
        this.scancode = scancode;
        this.action = action;
        this.mods = mods;
    }

    public int getKey() {
        return key;
    }

    public int getScancode() {
        return scancode;
    }

    public int getAction() {
        return action;
    }

    public int getMods() {
        return mods;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setScancode(int scancode) {
        this.scancode = scancode;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public void setMods(int mods) {
        this.mods = mods;
    }

    @Override
    public EventType getType() {
        return EventType.KEY;
    }

}