package ch.usi.inf.sa4.sphinx.model.conditions;

import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.MotionSensor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Condition targeting the on/off state of a device.
 */
@Entity
public class OnCondition extends  Condition<Boolean> {
    public enum Operator {
        ON,
        OFF;

        private boolean act(boolean a, boolean b) {
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

    /**
     *
     * @param device the device to target
     * @param target the status on/off
     * @param operator
     */
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
