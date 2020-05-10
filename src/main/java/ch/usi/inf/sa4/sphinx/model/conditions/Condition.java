package ch.usi.inf.sa4.sphinx.model.conditions;

import ch.usi.inf.sa4.sphinx.model.StorableE;
import lombok.NonNull;

import javax.persistence.Entity;

@Entity
public abstract class Condition<T> extends StorableE {
    protected T target;

    public Condition(T target) {
        this.target = target;
    }

    public abstract boolean check();
}
