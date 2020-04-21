package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.service.DeviceService;

public abstract class Event<T> extends Storable<Integer, Event<?>> {
    protected final int deviceId;
    protected DeviceService deviceService;

    public Event(Integer deviceId, DeviceService deviceService) {
        this.deviceId = deviceId;
        this.deviceService = deviceService;
    }

    public Event(Event<T> event){
        this.deviceId = event.deviceId;
        this.deviceService = event.deviceService;
        setKey(event.getKey());
    }

    public int getDeviceId() {
        return deviceId;
    }


    public abstract T get();


}
