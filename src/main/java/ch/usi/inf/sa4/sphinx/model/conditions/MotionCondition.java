package ch.usi.inf.sa4.sphinx.model.conditions;

import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.MotionSensor;
import ch.usi.inf.sa4.sphinx.model.Sensor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class MotionCondition extends Condition<MotionSensor> {


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

    protected Boolean target;
    private Operator operator;

    public MotionCondition(MotionSensor device, Boolean target, Operator operator) {
        super(device);
        this.target = target;
        this.operator = operator;
    }

    @Override
    public boolean check() {
        return operator.act(getDevice().isDetected(), target);
    }

}

