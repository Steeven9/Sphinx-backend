package ch.usi.inf.sa4.sphinx.model;

public abstract class Event<T> {
    public final int device;
    private Integer id;

    public int getDevice() {
        return device;
    }

    public boolean setId(Integer id) {
        if (this.id != null) {
            this.id = id;
            return true;
        }
        return false;
    }

    public Event(int deviceId) {
        device = deviceId;
    }

    public abstract T get();

    public Integer getId() {
        return id;
    }
}
