package ch.usi.inf.sa4.sphinx.model.sceneEffects;

import ch.usi.inf.sa4.sphinx.model.StorableE;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.view.SerialisableScene;
import ch.usi.inf.sa4.sphinx.view.SerialisableSceneEffect;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a Scene.
 * The Scene can contain Effect(s)
 */
@Entity
public class Scene extends StorableE implements Runnable {
    private String name;
    private String icon;
    @OneToMany(orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<SceneEffect> effects;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Scene(User user, final String name, final String icon) {
        this.user = user;
        this.effects = new ArrayList<>();
        if (name != null) {
            this.name = name;
        } else {
            this.name = "Scene";
        }

        if (icon != null) {
            this.icon = icon;
        } else {
            this.icon = "./img/icons/scenes/icon-generic-scene.svg";
        }

    }

    public Scene() {
        name = "Scene";
        effects = new ArrayList<>();
        icon = "./img/icons/scenes/icon-generic-scene.svg";
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Used to set the onwer of the Scene.
     *
     * @param user the owner of this Scene
     */
    public void setUser(final User user) {
        this.user = user;
    }

    /**
     * @return the User that owns this Scene
     * @see User
     */
    public User getUser() {
        return user;
    }


    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return this.icon;
    }

    public List<SceneEffect> getEffects() {
        return this.effects;
    }


    /**
     * @return a serialised version of this Scene
     */
    public SerialisableScene serialise() {
        final SerialisableScene ss = new SerialisableScene();
        List<SerialisableSceneEffect> sEffects = getEffects().stream().map(SceneEffect::serialise).collect(Collectors.toList());
        return new SerialisableScene(getId(), getName(), getIcon(), sEffects);
    }

    public void addEffect(SceneEffect effect) {
        effects.add(effect);
    }


    @Override
    public void run() {
        effects.forEach(SceneEffect::run);
    }

    public void clearEffects() {
        effects.clear();
    }


}
