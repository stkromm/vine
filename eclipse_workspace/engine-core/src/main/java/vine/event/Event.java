package vine.event;

public abstract class Event {
    public enum EventType {
        MOUSE_BUTTON, KEY
    }

    public abstract EventType getType();

    public void setHandled(boolean onEvent) {

    }

    public boolean isHandled() {
        // TODO Auto-generated method stub
        return false;
    }

}
