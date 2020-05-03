package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.NotImplementedException;
import ch.usi.inf.sa4.sphinx.service.DeviceService;
import com.google.gson.annotations.Expose;
import lombok.NonNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Couples an Effect with multiple Events.
 */
@Entity
public class Coupling extends StorableE {
    @Expose
    @OneToOne(cascade = CascadeType.ALL,
            orphanRemoval = true)
    private final Event event;

    @Expose
    @OneToMany(cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private final List<Effect> effects;


    private Integer deviceId;


    /**
     * Constructor for Coupling linking an event to multiple effects.
     * @param event the Event
     * @param effects the Effects linked to the {@code Event}
     * @see Event
     * @see Effect
     * @param <T> parametrized type of Event and Effect
     */
    public <T> Coupling(@NonNull Event<T> event,@NonNull Collection<Effect<T>> effects) {
        this.event = event;
        this.effects = new ArrayList<>(effects);
    }


    /**
     * Constructor for Coupling linking an Event to no Effects.
     * @param event the Event
     * @param <T> parametrized type of Event and Effect
     */
    public <T> Coupling(@NonNull Event<T> event){
        this(event, new ArrayList<>());
    }

    /**
     * Constructor for Coupling linking an Event to one Effect.
     * @param event the Event
     * @param effect a single Effect linked to the Event
     * @param <T> parametrized type of Event and Effect
     */
    public <T> Coupling(@NonNull Event<T> event,@NonNull Effect<T> effect){
        this(event, new ArrayList<>());
        addEffect(effect);
    }


    /**
     * @return The event associated with this Coupling
     * @see Event
     */
    public Event getEvent() {
        return event;
    }


    /**
     * @return The Effects associated with this Coupling
     * @see Effect
     */
    public List<Effect> getEffects() {
        return effects;
    }


    /**
     * @deprecated
     * @return The id of the Event associated twith this Coupling
     */
    public Integer getEventId() {
        return event.getId();
    }

    public void run() {
        for (Effect effect : effects) {
            effect.execute(event.get());
        }
    }


    /**
     * @deprecated
     * @return A list of the ids of all the Effects linked to this Coupling
     */
    public List<Integer> getEffectIds() {
        throw new NotImplementedException();
    }


    /**
     * Associates a new Effect to this Coupling
     * @param effect the Effect to add to this Coupling
     * @see Effect
     */
    public void addEffect(@NonNull Effect effect) {
        effects.add(effect);
    }

    @Override
    public boolean equals(@NonNull Object other) {
        if(!(other instanceof Coupling)) return false;
        if(other == this) return true;
        Coupling otherCoupling = (Coupling) other;
        if(id == null || otherCoupling.getId() == null) return false;
        return  id.equals(((Coupling) other).getId());
    }



}
