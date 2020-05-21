package ch.usi.inf.sa4.sphinx.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
public abstract class Observer<T extends Device> extends StorableE {
    public abstract void run();

    public Observer(T device) {
        this.device = device;
    }


    @ManyToOne(targetEntity = Device.class)
    @JoinColumn
    private T device;


    public T  getDevice() {
        return  device;
    }
}
