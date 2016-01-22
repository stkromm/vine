package vine.event;

import java.util.ArrayDeque;
import java.util.Deque;

import vine.event.Event.EventType;
import vine.graphics.Layer;
import vine.graphics.Scene;

public class EventDispatcher {
    private static Deque<Layer> listeners = new ArrayDeque<>();

    @FunctionalInterface
    public interface EventHandler {
        public boolean onEvent(Event event);
    }

    public static void registerListener(Layer layer) {
        listeners.addLast(layer);
    }

    public static void dispatch(Event event) {
        listeners.stream().filter(e -> e.onEvent(event)).findFirst();
    }
}