package ch.usi.inf.sa4.sphinx.model;

import javax.persistence.*;

//Must be concrete :/
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Observer<T extends Device> extends StorableE {
    public abstract void run();

    public Observer(T device) {
        this.device = device;
    }


    @ManyToOne(targetEntity = Device.class)
    @JoinColumn
    private T device;

    //    @ManyToOne
//    @JoinColumn(name = "device_id",
//            nullable = false,
//            referencedColumnName = "id"
//    )
    public T  getDevice() {
        return  device;
    }

}
