package ch.usi.inf.sa4.sphinx.model.conditions;

import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.StorableE;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public abstract class Condition<T extends Device> extends StorableE {

    @ManyToOne(targetEntity = Device.class)
    private T device;

    public Condition(T device) {
        this.device = device;
    }

    public abstract boolean check();


    public T getDevice() {
        return device;
    }
}
