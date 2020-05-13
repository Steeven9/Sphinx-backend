package ch.usi.inf.sa4.sphinx.model.events;

import ch.usi.inf.sa4.sphinx.model.Automation;
import ch.usi.inf.sa4.sphinx.model.Event;
import ch.usi.inf.sa4.sphinx.model.Sensor;
import ch.usi.inf.sa4.sphinx.model.conditions.SensorQuantityCondition;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class SensorChanged extends Event<Object> {
    @OneToOne(cascade = CascadeType.ALL)
    private SensorQuantityCondition condition;

    public SensorChanged(Sensor sensor, Automation automation, Double target, SensorQuantityCondition.Operator operator) {
        super(null, automation);
        condition = new SensorQuantityCondition(sensor, target, operator);
    }


    /**
     * If the value of the sensor has reached the target value the related Automation will run.
     */
    @Override
    public void run() {
        if (condition.check()) super.run();
    }

    /**
     * @deprecated
     * @return null
     */
    @Override
    public Object get() {
        return null;
    }
}
