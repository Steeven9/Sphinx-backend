package ch.usi.inf.sa4.sphinx.model.sceneEffects;

import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.StorableE;
import ch.usi.inf.sa4.sphinx.view.SerialisableSceneEffect;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
public abstract class SceneAction<T extends Device> extends StorableE implements Runnable{
    private String name;
    @ManyToMany(targetEntity = Device.class)
    @JoinColumn(name="device_id", referencedColumnName = "id")
    private List<T> devices;

    public SceneAction(List<T> devices, String name) {
        this.devices = devices;
        this.name = name;
    }



    public String getName() {
        return name;
    }

    public List<T> getDevices() {
        return devices;
    }

    public abstract SerialisableSceneEffect serialise();

    public abstract SceneType getType();

    public abstract void run();

}
