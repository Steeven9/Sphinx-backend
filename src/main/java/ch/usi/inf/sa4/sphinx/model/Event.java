package ch.usi.inf.sa4.sphinx.model;


import ch.usi.inf.sa4.sphinx.service.DeviceService;

public abstract class Event<T> extends StorableE {
    protected final int deviceId;
    protected DeviceService deviceService;

    public Event(Integer deviceId, DeviceService deviceService) {
        this.deviceId = deviceId;
        this.deviceService = deviceService;
    }

    public Event(Event<T> event){
        this.deviceId = event.deviceId;
        this.deviceService = event.deviceService;

    }

    public int getDeviceId() {
        return deviceId;
    }


    public abstract T get();


}
