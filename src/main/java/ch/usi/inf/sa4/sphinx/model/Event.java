package ch.usi.inf.sa4.sphinx.model;

public abstract class Event<T> {
    public final int device;

    public Event(int deviceId) {
        device = deviceId;
    }

    public abstract T get();
}
