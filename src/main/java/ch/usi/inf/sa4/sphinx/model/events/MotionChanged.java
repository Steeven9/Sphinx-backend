package ch.usi.inf.sa4.sphinx.model.events;

import ch.usi.inf.sa4.sphinx.model.Automation;
import ch.usi.inf.sa4.sphinx.model.Event;
import ch.usi.inf.sa4.sphinx.model.MotionSensor;
import ch.usi.inf.sa4.sphinx.model.conditions.MotionCondition;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;


@Entity
public class MotionChanged extends Event {

    @OneToOne(cascade = CascadeType.ALL)
    private MotionCondition condition;

    public MotionChanged(MotionSensor sensor, Automation automation, Boolean target, MotionCondition.Operator operator) {
        super(null, automation);
        condition = new MotionCondition(sensor, target,operator);
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
