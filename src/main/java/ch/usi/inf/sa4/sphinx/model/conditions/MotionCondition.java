package ch.usi.inf.sa4.sphinx.model.conditions;

import ch.usi.inf.sa4.sphinx.model.MotionSensor;
import ch.usi.inf.sa4.sphinx.model.triggers.ConditionType;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class MotionCondition extends Condition<MotionSensor> {


    public enum Operator {
        EQUAL,
        NOT_EQUAL;

        private boolean act(Boolean a, Boolean b) {
            switch (this) {
                case EQUAL:
                    return a.equals(b);
                case NOT_EQUAL:
                    return !a.equals(b);
            }
            return false;
        }
    }

    protected Boolean target;
    private Operator operator;


    /**
     * Constructor.
     * The condition is build with a target device, a target value and an Operator to apply to the condition
     * such as GREATER to check that the stored value is greater than the target value.
     * @param device the device to target
     * @param target the value to target
     * @param operator the operator to apply
     */
    public MotionCondition(MotionSensor device, Boolean target, Operator operator) {
        super(device);
        this.target = target;
        this.operator = operator;
    }


    /**
     * @inheritDoc
     */
    @Override
    public boolean check() {
        return operator.act(getDevice().isDetected(), target);
    }


    /**
     * @inheritDoc
     */
    @Override
    public ConditionType getConditionType() {
        return operator == Operator.EQUAL? ConditionType.MOTION_DETECTED: ConditionType.MOTION_NOT_DETECTED;
    }


    /**
     * @inheritDoc
     */
    @Override
    protected String getStringValue() {
        return target.toString();
    }
}

