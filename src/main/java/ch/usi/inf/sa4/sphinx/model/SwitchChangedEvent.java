package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.controller.Storage;

public class SwitchChangedEvent extends Event<Boolean>{
    public final int device;
    private boolean satisfyingState;

    public SwitchChangedEvent(int deviceID){
        super(deviceID);
        this.device = deviceID;
    }


    @Override
    public Boolean get() {
        return ((Switch)Storage.getDevice(device)).getPower()== 1;
    }
}
