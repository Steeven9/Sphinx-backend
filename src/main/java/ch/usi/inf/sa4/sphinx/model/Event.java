package ch.usi.inf.sa4.sphinx.model;

public abstract class Event<T> extends Storable<Integer, Event<?> > {
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

    @Override
    public Event<T> makeCopy() {
        return null;
    }

    public Integer getId() {
        return getKey();
    }
}
