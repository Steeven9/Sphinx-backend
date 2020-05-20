package ch.usi.inf.sa4.sphinx.model.conditions;

import ch.usi.inf.sa4.sphinx.model.Sensor;

import javax.persistence.Entity;

@Entity
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

    public SensorQuantityCondition(Sensor device, Double target, Operator operator) {
        super(device);
        this.target = target;
        this.operator = operator;
    }

    @Override
    public boolean check() {
        return operator.act(getDevice().getStatus(), target);
    }

}
