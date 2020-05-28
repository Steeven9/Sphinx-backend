package ch.usi.inf.sa4.sphinx.model;
import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import com.google.gson.annotations.Expose;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Lob;

/**
 * Represents a security camera.
 */
@Entity
public class SecurityCamera extends Device {
    @Expose
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    public String video;

    /**
     * Constructor.
     * Creates a camera.
     */
    public SecurityCamera() {
        super();
    }

    @Override
    public void setPropertiesFrom(SerialisableDevice sd) {
        super.setPropertiesFrom(sd);
        video = sd.getVideo();
    }

    @Override
    public DeviceType getDeviceType() {
        return DeviceType.SECURITY_CAMERA;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return "" + isOn();
    }

    public String getVideo() {
        return video;
    }


}
