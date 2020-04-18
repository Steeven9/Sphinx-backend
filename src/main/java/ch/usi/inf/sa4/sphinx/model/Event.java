package ch.usi.inf.sa4.sphinx.model;

public abstract class Event<T> extends StorableE {
    protected final int deviceId;

    public Event(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Event(Event<T> event){
        this.deviceId = event.deviceId;
    }

    public int getDeviceId() {
        return deviceId;
    }


    public abstract T get();


}
