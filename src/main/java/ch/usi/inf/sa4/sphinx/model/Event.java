package ch.usi.inf.sa4.sphinx.model;

public abstract class Event<T> extends Storable<Integer, Event<?>> {
    public Event(Integer deviceId) {
        setKey(deviceId);
    }

    public abstract T get();


}
