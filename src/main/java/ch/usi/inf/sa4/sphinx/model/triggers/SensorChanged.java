package ch.usi.inf.sa4.sphinx.model.triggers;

import ch.usi.inf.sa4.sphinx.model.Automation;
import ch.usi.inf.sa4.sphinx.model.Sensor;
import ch.usi.inf.sa4.sphinx.model.conditions.SensorQuantityCondition;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class SensorChanged extends Trigger{

    public SensorChanged(Sensor sensor, Automation automation, Double target, SensorQuantityCondition.Operator operator) {
        super(automation, new SensorQuantityCondition(sensor, target, operator));
    }
}
