package vine.event;

public class ScrollEvent implements Event
{

    @Override
    public EventType getType()
    {
        return EventType.SCROLL;
    }

}
