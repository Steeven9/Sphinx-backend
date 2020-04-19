package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;

public class SwitchChangedEvent extends Event<Double> {

    @Autowired
    private DeviceService deviceService;

    /**
     * Constructor.
     *
     * @param deviceId the id of a device
     */
    public SwitchChangedEvent(Integer deviceId) {
        super(deviceId);
    }

    private SwitchChangedEvent(SwitchChangedEvent other) {
        super(other);
    }

    /**
     * Gets current state of device
     *
     * @return 1.0 if the switch is on, 0.0 otherwise
     **/
    @Override
    public Double get() {
        return ((Switch) deviceService.get(deviceId)).isOn() ? 1.0 : 0.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Event<Double> makeCopy() {
        return new SwitchChangedEvent(this);
    }
}
