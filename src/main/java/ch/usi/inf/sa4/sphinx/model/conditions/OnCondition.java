package ch.usi.inf.sa4.sphinx.model.conditions;

import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.MotionSensor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class OnCondition extends  Condition<Boolean> {
    public enum Operator {
        ON,
        OFF;

        private boolean act(Boolean a, Boolean b) {
            switch (this) {
                case ON:
                    return a == b;
                case OFF:
                    return a != b;
            }
            return false;
        }
    }

    @ManyToOne
    private Device device;
    private Boolean target;
    private Operator operator;

    public OnCondition(Device device, Boolean target, Operator operator) {
        this.target = target;
        this.device = device;
        this.operator = operator;
    }

    @Override
    public boolean check() {
        return operator.act(device.isOn(), target);
    }
}
