package ch.usi.inf.sa4.sphinx.model.conditions;

import ch.usi.inf.sa4.sphinx.model.Sensor;
import ch.usi.inf.sa4.sphinx.model.triggers.ConditionType;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

/**
 * Represents a condition on a Sensor quantity.
 */
@Entity
@NoArgsConstructor
public class SensorQuantityCondition extends Condition<Sensor> {
    public enum Operator {
        GREATER,
        SMALLER,
        EQUAL;

        public boolean act(Number a, Number b) {
            switch (this) {
                case EQUAL:
                    return a.doubleValue() == b.doubleValue();
                case GREATER:
                    return a.doubleValue() > b.doubleValue();
                case SMALLER:
                    return a.doubleValue() < b.doubleValue();

            }
            return false;
        }
    }

    private Operator operator;
    private Double target;

    /**
     * Constructor.
     * The condition is build with a target device, a target value and an Operator to apply to the condition
     * such as GREATER to check that the stored value is greater than the target value.
     * @param device the device to target
     * @param target the value to target
     * @param operator the operator to apply
     */
    public SensorQuantityCondition(Sensor device, Double target, Operator operator) {
        super(device);
        this.target = target;
        this.operator = operator;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean check() {
        return operator.act(getDevice().getStatus(), target);
    }

    /**
     * @inheritDoc
     */
    @Override
    public ConditionType getConditionType() {
        return operator == Operator.GREATER ? ConditionType.SENSOR_OVER : ConditionType.SENSOR_UNDER;
    }

    /**
     * @inheritDoc
     */
    @Override
    protected String getStringValue() {
        return target.toString();
    }

//    @Override
//    public boolean equals(Object obj) {
//        return th
//    }
}
