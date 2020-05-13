package ch.usi.inf.sa4.sphinx.model.events;

import ch.usi.inf.sa4.sphinx.model.Automation;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Event;
import ch.usi.inf.sa4.sphinx.model.conditions.OnCondition;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class OnChanged extends Event {

    @OneToOne(cascade = CascadeType.ALL)
    private OnCondition condition;

    public OnChanged(Device device, Automation automation, Boolean target, OnCondition.Operator operator) {
        super(null, automation);
        condition = new OnCondition(device, target,operator);
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
