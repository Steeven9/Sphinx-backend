package ch.usi.inf.sa4.sphinx.model;


import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Effect<T> extends StorableE {
    protected final int deviceId;

    public Effect(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public abstract void execute(T effect);
}
