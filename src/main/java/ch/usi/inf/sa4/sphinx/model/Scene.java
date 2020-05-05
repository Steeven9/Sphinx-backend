package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.view.SerialisableScene;
import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Scene.
 * The Scene can contain Effect(s)
 */
public class Scene implements Runnable{
    @Expose
    private String name;
    @Expose
    private String icon;
    @Expose
    @OneToMany(orphanRemoval = true,
            cascade = CascadeType.ALL,
            mappedBy = "scene",
            fetch = FetchType.LAZY)
    private List<Effect> effects;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Scene(List<Effect> effects, String name, String icon){
        this.effects = effects;
        this.name = name;
        this.icon = icon;

    }

    public Scene(){
        name = "Scene";
        effects = new ArrayList<>();
        icon = "./img/icons/scenes/icon-generic-scene.svg";
    }

    public Scene(SerialisableScene serialisableScene) {
        this();
        if(serialisableScene.name != null) this.name = serialisableScene.name;
        if(serialisableScene.icon != null) this.icon = serialisableScene.icon;
    }

    public String getName(){
        return this.name;
    }
    /**
     * Used to set the onwer of the Scene.
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

    public String getIcon(){
        return this.icon;
    }

    public List<Effect> getEffects() {
        return this.effects;
    }


    /**
     * @return a serialised version of this Scene
     */
    public SerialisableScene serialise(){
        final SerialisableScene ss = new SerialisableScene();
        ss.effects = getEffects();
        ss.icon = getIcon();
        ss.name = getName();
        return ss;

    }

    @Override
    public void run() {

    }
}
