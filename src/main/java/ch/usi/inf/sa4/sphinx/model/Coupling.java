package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.service.DeviceService;
import com.google.gson.annotations.Expose;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


//TODO fix atm couplings of different parameters can be created
@Entity
public class Coupling extends StorableE{

    @Autowired
    DeviceService deviceService;

    @Expose
    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "device_id")
    private final Device device;

    @Expose
    @OneToOne(cascade = CascadeType.ALL,
    orphanRemoval = true,
    mappedBy = "coupling")
    private  Event event;

    @Expose
    @OneToMany(cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "coupling"
    )
    private final List<Effect> effects;


    public Coupling(Device device) {
        this.device = device;
        this.effects = new ArrayList<>();
    }


    public void setEvent(Event event) {
        this.event = event;
    }




    /**
     * sets the id of this object to the given id
     * @param key the id to set
     * @return true if the id is set false otherwise if it has already been set
     */
    public boolean setId(int key){
        return setKey(key);
    }

    /**
     * gettter for  id
     * @return the id of the coupling
     */
    public Integer getId() {
        return getKey();
    }


    public Integer getEventId() {
        return eventId;
    }

    public List<Integer> getEffectIds() {
        return effectIds;
    }

    public void addEffect(Integer effect){
        effectIds.add(effect);
    }


}
