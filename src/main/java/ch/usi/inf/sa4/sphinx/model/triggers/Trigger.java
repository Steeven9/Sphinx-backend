package ch.usi.inf.sa4.sphinx.model.triggers;

import ch.usi.inf.sa4.sphinx.model.Automation;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Observer;
import ch.usi.inf.sa4.sphinx.model.conditions.Condition;
import ch.usi.inf.sa4.sphinx.view.SerialisableCondition;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

/**
 * A Trigger works to activate a Scene.
 * The trigger class is used in order to trigger a scene when the specified condition
 * becomes true.
 */
@Entity
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Trigger extends Observer<Device> {
    @ManyToOne
    @JoinColumn(name = "automation_id", nullable = false, referencedColumnName = "id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
    private Automation automation;
    @OneToOne(cascade = CascadeType.ALL)
    private Condition condition;


    /**
     * Targets a determinate automation to run it when the condition becomes true.
     * @param automation the automation to target
     * @param condition the condition that needs to be verified
     */
    public Trigger(Automation automation, Condition condition) {
        super(condition.getDevice());
        this.automation = automation;
        this.condition = condition;
    }

    /**
     * Triggers the trigger making the targeted Automation run.
     */
    @Override
    public void run() {
        if (condition.check()) automation.run();
    }


    /**
     * @return The serialised condition of this Trigger.
     */
    public SerialisableCondition serialise() {
        return condition.serialise();
    }

    public ConditionType getConditionType(){
        return condition.getConditionType();
    }
}
