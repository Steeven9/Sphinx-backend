package ch.usi.inf.sa4.sphinx.model;
import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.service.CouplingService;
import ch.usi.inf.sa4.sphinx.service.RoomService;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;

public class SecurityCamera extends Device {
    public String url;

    /**
     * Constructor.
     * Creates a camera.
     */
    public SecurityCamera() {
        super();
    }

    private SecurityCamera(SecurityCamera other){
        super();
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

    @Override
    protected DeviceType getDeviceType() {
        return DeviceType.SECURITY_CAMERA;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return isOn() + " URL:"+ getUrl();
    }


    public Device makeCopy() {
        return new SecurityCamera(this);
    }
}
