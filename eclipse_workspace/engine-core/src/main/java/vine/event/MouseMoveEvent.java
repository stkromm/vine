package vine.event;

public class MouseMoveEvent implements Event {

    @Override
    public EventType getType() {
        return EventType.MOUSE_MOVE;
    }

}
