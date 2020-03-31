package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


//TODO fix atm couplings of different parameters can be created
/** Handles interaction among Events and Effects **/
public class Coupling {

    @Autowired
    DeviceService deviceService;

    private Integer id;
    private final Integer eventId;
    private final List<Integer> effectIds;


    /**
     * Constructor.
     * @param eventID, id of the event
     * @param effectId, id of the effect
     */
    public Coupling(Integer eventId, Integer effectId) {
        this.eventId = eventId;
        this.effectIds =new ArrayList<>();
        effectIds.add(effectId);
    }

    /**
     * Constructor.
     * @param eventID, id of the event
     * @param effectIds, list of ids of the effect
     */
    public Coupling(Integer eventId, Collection<Integer> effectIds){
        this.eventId = eventId;
        this.effectIds = new ArrayList<>();
        this.effectIds.addAll(effectIds);
    }

    /**
     * Sets the id of this object to the given id.
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
     * Getter for the id.
     * @return the id of the coupling
     */
    public Integer getId() {
        return id;
    }

    /**
     * Getter for the event id.
     * @return the id of the event
     */
    public Integer getEventId() {
        return eventId;
    }

    /**
     * Getter for the event id.
     * @return the list of ids for the effects
     */
    public List<Integer> getEffectIds() {
        return effectIds;
    }

    /**
     * Adds an effect to the list of effects.
     * @param effect the id of the effect to add
     */
    public void addEffect(Integer effect){
        effectIds.add(effect);
    }

    /**
     * Makes a copy of the a Coupling object.
     * @return the copy of the Coupling object
     */
    public Coupling makeCopy(){
        Coupling cp = new Coupling(eventId, effectIds);
        cp.id = this.id;
        return cp;
    }

}
