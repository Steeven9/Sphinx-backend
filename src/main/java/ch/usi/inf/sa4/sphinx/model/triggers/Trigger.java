package ch.usi.inf.sa4.sphinx.model.triggers;

import ch.usi.inf.sa4.sphinx.model.Automation;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Observer;
import ch.usi.inf.sa4.sphinx.model.conditions.Condition;
import ch.usi.inf.sa4.sphinx.view.SerialisableCondition;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * Del
 */
@Entity
public abstract class Trigger extends Observer<Device> {
    @ManyToOne
    private Automation automation;
    @OneToOne(cascade = CascadeType.ALL)
    private Condition condition;


    public Trigger(Automation automation, Condition condition) {
        super(condition.getDevice());
        this.automation = automation;
        this.condition = condition;
    }

    @Override
    public void run() {
        if (condition.check()) automation.run();
        //System.out.println("HEEEEEERE TIRGGER");
    }


    public SerialisableCondition serialise() {

        return condition.serialise();
    }
}
