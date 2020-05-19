package ch.usi.inf.sa4.sphinx.model.triggers;

import ch.usi.inf.sa4.sphinx.model.Automation;
import ch.usi.inf.sa4.sphinx.model.Observer;
import ch.usi.inf.sa4.sphinx.model.conditions.Condition;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public abstract class Trigger extends Observer {
    @ManyToOne
    private Automation automation;
    @OneToOne(cascade = CascadeType.ALL)
    private Condition condition;


    public Trigger(Automation automation, Condition condition) {
        this.automation = automation;
        this.condition = condition;
    }

    @Override
    public void run() {
        if (condition.check()) automation.run();
    }

}
