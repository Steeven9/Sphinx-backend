package ch.usi.inf.sa4.sphinx.model.conditions;

import ch.usi.inf.sa4.sphinx.model.MotionSensor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class MotionCondition extends Condition<Boolean> {


    public enum Operator {
        EQUAL,
        NOT_EQUAL;

        private boolean act(Boolean a, Boolean b) {
            switch (this) {
                case EQUAL:
                    return a == b;
                case NOT_EQUAL:
                    return a != b;
            }
            return false;
        }
    }

    @ManyToOne
    private MotionSensor device;
    protected Boolean target;
    private Operator operator;

    public MotionCondition(MotionSensor device, Boolean target, Operator operator) {
        super();
        this.target = target;
        this.device = device;
        this.operator = operator;
    }

    @Override
    public boolean check() {
        return operator.act(device.isDetected(), target);
    }
}

