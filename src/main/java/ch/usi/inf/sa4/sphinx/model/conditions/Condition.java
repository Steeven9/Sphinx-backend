package ch.usi.inf.sa4.sphinx.model.conditions;

import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.StorableE;
import ch.usi.inf.sa4.sphinx.model.triggers.ConditionType;
import ch.usi.inf.sa4.sphinx.view.SerialisableCondition;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

/**
 * Represents a condition on a Device ex: the Device is off
 * @param <T> The type of the Device being targeted by this condition
 * @see Device
 */
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@NoArgsConstructor
public abstract class Condition<T extends Device> extends StorableE {

    @ManyToOne(targetEntity = Device.class)
    private T device;

    /**
     * @param device The device that will be a target for this condition
     */
    public Condition(T device) {
        this.device = device;
    }

    /**
     * @return true if the condition is satisfied else false
     */
    public abstract boolean check();


    /**
     * @return a serialized version of this condition
     */
    public SerialisableCondition serialise() {
        return new SerialisableCondition(getConditionType(), device.getId(), getStringValue());
    }

    /**
     * @return The device targeted by this condition
     */
    public T getDevice() {
        return device;
    }


    /**
     * @return The type of this condition.
     */
    public abstract ConditionType getConditionType();

    protected abstract String getStringValue();
}


