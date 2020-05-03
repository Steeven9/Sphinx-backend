package ch.usi.inf.sa4.sphinx.model;


import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * This class reproduces a generic effect on a given Device
 * @param <T> The type used to execute the Effect.
 * @see Device
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Effect<T> extends StorableE {
    protected final int deviceId;

    /**
     * Constructor
     * @param deviceId the id of the device this Effect affects
     */
    public Effect(Integer deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * Executes given an effect to apply
     * @param effect the effect to apply
     */
    public abstract void execute(T effect);

    public Integer getDeviceId() {
        return deviceId;
    }
}
