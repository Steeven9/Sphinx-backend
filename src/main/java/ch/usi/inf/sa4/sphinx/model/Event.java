package ch.usi.inf.sa4.sphinx.model;

public abstract class Event<T> extends Storable<Integer, Event<?>> {
    protected final int deviceId;

    public Event(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Event(Event<T> event){
        this.deviceId = event.deviceId;
        setKey(event.getKey());
    }

    public int getDeviceId() {
        return deviceId;
    }


    public abstract T get();


}
