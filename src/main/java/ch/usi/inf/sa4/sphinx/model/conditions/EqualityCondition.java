package ch.usi.inf.sa4.sphinx.model.conditions;

import ch.usi.inf.sa4.sphinx.misc.StatusHolder;

import javax.persistence.ManyToOne;

public class EqualityCondition extends Condition<Object> {
    @ManyToOne
    private StatusHolder<?> device;

    public EqualityCondition(StatusHolder<?> device, Object target) {
        super(target);
        this.device = device;
    }

    @Override
    public boolean check() {
        return device.getStatus().equals(target);
    }
}