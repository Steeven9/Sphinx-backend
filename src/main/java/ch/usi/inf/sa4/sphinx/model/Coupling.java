package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.NotImplementedException;
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
public class Coupling extends StorableE {

    @Autowired
    @Transient
    DeviceService deviceService;

    @Expose
    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "device_id")
    private Device device;

    @Expose
    @OneToOne(cascade = CascadeType.ALL,
            orphanRemoval = true)
    private final Event event;

    @Expose
    @OneToMany(cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private final List<Effect> effects;


    public Coupling(Device device, Event event, Collection<Effect> effects) {
        this.device = device;
        this.event = event;
        this.effects = new ArrayList<>(effects);
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


    public void addEffect(Effect effect) {
        effects.add(effect);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Coupling) return this.getId().equals(((Coupling) other).getId());
        return false;
    }


}
