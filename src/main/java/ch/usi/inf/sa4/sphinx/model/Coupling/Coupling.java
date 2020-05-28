package ch.usi.inf.sa4.sphinx.model.Coupling;

import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Observer;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Pairs two devices, one will affect the other.
 * @param <X> Type of the Device observed by the Coupling
 * @param <Y> Type of the Device affected by the Coupling
 */
@Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor
public abstract class Coupling<X extends Device, Y extends Device> extends Observer<X> {
    @ManyToOne(targetEntity = Device.class)
    private Y device2;

    /**
     * @param observed the Device to be observed
     * @param affected the Device to be affected
     */
    public Coupling(X observed, Y affected) {
        super(observed);
        this.device2 = affected;
    }

    /**
     * @return the observed device
     */
    public X getObserved() {
        return  getDevice();
    }

    /**
     * @return the affected device
     */
    public Y getAffected() {
        return device2;
    }



}
