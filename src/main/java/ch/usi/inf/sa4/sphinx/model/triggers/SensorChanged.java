package ch.usi.inf.sa4.sphinx.model.triggers;

import ch.usi.inf.sa4.sphinx.model.Automation;
import ch.usi.inf.sa4.sphinx.model.Sensor;
import ch.usi.inf.sa4.sphinx.model.conditions.SensorQuantityCondition;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

/**
 * Trigger for changes in the status of a sensor.
 */
@Entity
@NoArgsConstructor
public class SensorChanged extends Trigger{

    /**
     * @param sensor the sensor to target
     * @param automation the automation to run
     * @param target the target value the sensor has to reach
     * @param operator the operator to apply to the target value
     */
    public SensorChanged(Sensor sensor, Automation automation, Double target, SensorQuantityCondition.Operator operator) {
        super(automation, new SensorQuantityCondition(sensor, target, operator));
    }
}
