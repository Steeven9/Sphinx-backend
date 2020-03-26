package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


//TODO fix atm couplings of different parameters can be created
public class Coupling {

    @Autowired
    DeviceService deviceService;

    private Integer id;
    private final Integer eventId;
    private final List<Integer> effectIds;


    public Coupling(Integer eventId, Integer effectId) {
        this.eventId = eventId;
        this.effectIds =new ArrayList<>();
        effectIds.add(effectId);
    }

    public Coupling(Integer eventId, Collection<Integer> effectIds){
        this.eventId = eventId;
        this.effectIds = new ArrayList<>();
        this.effectIds.addAll(effectIds);
    }

    /**
     * sets the id of this object to the given id
     * @param id the id to set
     * @return true if the id is set false otherwise if it has already been set
     */
    public boolean setId(int id){
        if(this.id ==  null){
            this.id = id;
            return true;
        }
        return false;
    }

    /**
     * gettter for  id
     * @return the id of the coupling
     */
    public Integer getId() {
        return id;
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

    public Coupling makeCopy(){
        Coupling cp = new Coupling(eventId, effectIds);
        cp.id = this.id;
        return cp;
    }

}
