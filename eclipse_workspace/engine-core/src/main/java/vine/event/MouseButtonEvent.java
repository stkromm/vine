package vine.event;

public class MouseButtonEvent extends Event {
    int button;
    int action;
    int mods;
    double x;
    double y;

    public MouseButtonEvent(int button, int action, int mods, double x, double y) {
        super();
        this.button = button;
        this.action = action;
        this.mods = mods;
        this.x = x;
        this.y = y;
    }

    public int getButton() {
        return button;
    }

    public int getAction() {
        return action;
    }

    public int getMods() {
        return mods;
    }

    public void setButton(int button) {
        this.button = button;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public void setMods(int mods) {
        this.mods = mods;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public EventType getType() {
        return EventType.MOUSE_BUTTON;
    }

}