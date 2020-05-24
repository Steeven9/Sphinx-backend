package ch.usi.inf.sa4.sphinx.model.scenesEffects;

import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Scene;
import ch.usi.inf.sa4.sphinx.model.StorableE;
import ch.usi.inf.sa4.sphinx.view.SerialisableSceneEffect;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class SceneEffect<T extends Device> extends StorableE implements Runnable{
    public SceneEffect() {
    }

    public SceneEffect(List<T> devices, String name) {
        this.devices = devices;
        this.name = name;
    }

    private String name;

    @ManyToMany()
    private List<T> devices;


    public String getName() {
        return name;
    }

    public List<T> getDevices() {
        return devices;
    }

    public SceneEffect(List<T> devices) {
        this.devices = devices;
    }

    public abstract SerialisableSceneEffect serialise();

    public abstract SceneTypes getType();

}
