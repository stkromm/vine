package vine.event;

public class SensorChangeEvent implements Event {
    @Override
    public EventType getType() {
        return EventType.SENSOR;
    }

}
