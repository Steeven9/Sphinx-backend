package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.service.CouplingService;
import ch.usi.inf.sa4.sphinx.service.RoomService;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;

public class SecurityCamera extends Device {
    public String url;

    /**
     * Constructor.
     * Creates a camera.
     */
    public SecurityCamera(RoomService roomService, CouplingService couplingService) {
        super(roomService, couplingService);
    }

    private SecurityCamera(SecurityCamera other){
        super(other);
        this.url = other.getUrl();
    }

    public String getUrl(){
        return this.url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SerialisableDevice serialise() {
        SerialisableDevice s = super.serialise();
        s.url = getUrl();
        return s;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return isOn() + " URL:"+ getUrl();
    }

    @Override
    public Device makeCopy() {
        return new SecurityCamera(this);
    }
}
