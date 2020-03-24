package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.controller.Storage;
import ch.usi.inf.sa4.sphinx.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Coupling<T> {
    @Autowired
    DeviceService deviceService;

    private Integer id;
    private final Event<T> event;
    private final List<Effect<T>> effects = new ArrayList<>();

    @SafeVarargs
    public Coupling(Event<T> event, Effect<T> ...effects) {
        this.event = event;
        this.effects.addAll(Arrays.asList(effects));
        deviceService.get(event.device).addObserver(() -> {for (Effect<T> effect : effects) effect.execute(event.get());});

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

    //TODO
    public Coupling makeCopy(){
        return this;
    }

}
