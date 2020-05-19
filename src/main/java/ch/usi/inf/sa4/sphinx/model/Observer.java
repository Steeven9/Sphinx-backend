package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.NotImplementedException;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

//Must be concrete :/
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Observer extends StorableE {
    @Deprecated
    public void run() {
        throw new NotImplementedException();
    }

    ;
}
