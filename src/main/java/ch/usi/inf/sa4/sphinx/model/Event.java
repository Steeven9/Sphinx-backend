package ch.usi.inf.sa4.sphinx.model;

public abstract class Event<T> extends Storable<Integer> {
    public final int device;

    public int getDevice() {
        return device;
    }

    public boolean setId(Integer id) {
        return setId(id);
    }

    public Event(int deviceId) {
        device = deviceId;
    }

    public abstract T get();

    public Integer getId() {
        return getKey();
    }
}
