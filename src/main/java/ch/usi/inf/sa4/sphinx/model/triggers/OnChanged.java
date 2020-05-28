package ch.usi.inf.sa4.sphinx.model.triggers;

import ch.usi.inf.sa4.sphinx.model.Automation;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.conditions.OnCondition;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

/**
 * Trigger to observe the on status of device.
 */
@Entity
@NoArgsConstructor
public class OnChanged extends Trigger {
    /**
     * @param device the device to target
     * @param automation the automation to run
     * @param operator the target value to reach
     */
    public OnChanged(Device device, Automation automation, OnCondition.Operator operator) {
        super(automation,  new OnCondition(device, operator));
    }


}
