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


@Entity
public class Coupling extends StorableE {
    @Autowired
    @Transient
    DeviceService deviceService;

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



    public <T> Coupling(@NonNull Event<T> event,@NonNull Collection<Effect<T>> effects) {
        this.event = event;
        this.effects = new ArrayList<>(effects);
    }

    public <T> Coupling(Event<T> event){
        this(event, new ArrayList<>());
    }

    public <T> Coupling(Event<T> event, Effect<T> effect){
        this(event, new ArrayList<>());
        addEffect(effect);
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public Event getEvent() {
        return event;
    }


    public List<Effect> getEffects() {
        return effects;
    }



    public Integer getEventId() {
        return event.getId();
    }

    public void run() {
        for (Effect effect : effects) {
            effect.execute(event.get());
        }
    }


    public List<Integer> getEffectIds() {
        throw new NotImplementedException();
    }


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
