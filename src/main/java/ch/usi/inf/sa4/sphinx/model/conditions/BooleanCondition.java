package ch.usi.inf.sa4.sphinx.model.conditions;

import ch.usi.inf.sa4.sphinx.misc.StatusHolder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class BooleanCondition extends Condition<Boolean> {
    @ManyToOne
    private StatusHolder<Boolean> device;

    public BooleanCondition(StatusHolder<Boolean> device, Boolean target) {
        super(target);
        this.device = device;
    }

    @Override
    public boolean check() {
        return device.getStatus() == target;
    }
}
