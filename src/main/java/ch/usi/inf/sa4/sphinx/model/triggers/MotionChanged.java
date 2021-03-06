package ch.usi.inf.sa4.sphinx.model.triggers;

import ch.usi.inf.sa4.sphinx.model.Automation;
import ch.usi.inf.sa4.sphinx.model.MotionSensor;
import ch.usi.inf.sa4.sphinx.model.conditions.MotionCondition;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;


@Entity
@NoArgsConstructor
public class MotionChanged extends Trigger {


    public MotionChanged(MotionSensor sensor, Automation automation, Boolean target, MotionCondition.Operator operator) {
        super(automation,  new MotionCondition(sensor, target,operator));
    }

}
