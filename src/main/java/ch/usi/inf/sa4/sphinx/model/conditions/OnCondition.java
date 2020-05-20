package ch.usi.inf.sa4.sphinx.model.conditions;

import ch.usi.inf.sa4.sphinx.model.Device;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Condition targeting the on/off state of a device.
 */
@Entity
public class OnCondition extends Condition<Device> {
    public enum Operator {
        ON,
        OFF;

        private boolean act(boolean a) {
            switch (this) {
                case ON:
                    return a;
                case OFF:
                    return !a;
            }
            return false;
        }
    }

    private Operator operator;

    /**
     * @param device   the device to target
     * @param operator ON/OFF depending on the condition to check
     */
    public OnCondition(Device device, Operator operator) {
//        this.target = target;
        super(device);
        this.operator = operator;
    }


    @Override
    public boolean check() {
        return operator.act(getDevice().isOn());
    }
}
