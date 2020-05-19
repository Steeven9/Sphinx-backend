package ch.usi.inf.sa4.sphinx.model.triggers;

import ch.usi.inf.sa4.sphinx.model.Automation;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.conditions.OnCondition;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class OnChanged extends Trigger {

    public OnChanged(Device device, Automation automation, OnCondition.Operator operator) {
        super(automation,  new OnCondition(device, operator));
    }


}
