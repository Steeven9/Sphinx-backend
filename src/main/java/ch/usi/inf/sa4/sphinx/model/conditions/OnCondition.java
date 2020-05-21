package ch.usi.inf.sa4.sphinx.model.conditions;

import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.triggers.ConditionType;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

/**
 * Condition targeting the on/off state of a device.
 */
@Entity
@NoArgsConstructor
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



    /**
     * @inheritDoc
     */
    @Override
    public boolean check() {
        return operator.act(getDevice().isOn());
    }

    /**
     * @inheritDoc
     */
    @Override
    public ConditionType getEventType() {
        return operator == Operator.ON? ConditionType.DEVICE_ON: ConditionType.DEVICE_OFF;
    }

    /**
     * @inheritDoc
     */
    @Override
    protected String getStringValue() {
        return operator == Operator.ON? "true":"false";
    }
}
