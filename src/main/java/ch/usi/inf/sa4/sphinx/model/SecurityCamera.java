package ch.usi.inf.sa4.sphinx.model;
import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import com.google.gson.annotations.Expose;

import javax.persistence.Entity;

/**
 * Represents a security camera.
 */
@Entity
public class SecurityCamera extends Device {
    @Expose
    public String url;

    /**
     * Constructor.
     * Creates a camera.
     */
    public SecurityCamera() {
        super();
    }


    public String getUrl(){
        return this.url;
    }

    public void setUrl(final String url){
        this.url = url;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SerialisableDevice serialise() {
        final SerialisableDevice device = super.serialise();
        device.url = url;
        return device;
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
        return isOn() + " URL: " + url;
    }


}
