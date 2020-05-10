package ch.usi.inf.sa4.sphinx.model.conditions;

import ch.usi.inf.sa4.sphinx.misc.Operator;
import ch.usi.inf.sa4.sphinx.misc.StatusHolder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class NumberCondition extends Condition<Number> {
    @ManyToOne
    private StatusHolder<? extends Number> device;
    private Operator operator;

    public NumberCondition(StatusHolder<? extends Number> device, Operator operator, Number target) {
        super(target);
        this.device = device;
        this.operator = operator;
    }

    @Override
    public boolean check() {
        return operator.act(device.getStatus(), target);
    }
}
