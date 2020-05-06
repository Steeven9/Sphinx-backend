package ch.usi.inf.sa4.sphinx.model;

<<<<<<< HEAD
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
=======
public class Scene{
    private String name;
    private String icon;
    private Effect<?> effect;

    public Scene(Effect<?> e, String n, String i){
        this.effect = e;
        this.name = n;
        this.icon = i;
    }

    private Scene (Scene s){
        this.effect = s.getEffect();
        this.name = s.getName();
        this.icon = s.getIcon();
    }

    public String getName(){
        return this.name;
    }


    public String getIcon(){
        return this.icon;
    }

    public Effect<?> getEffect(){
        return this.effect;
>>>>>>> #73: modified class Scene and now is not a Device
    }

    public String getIcon(){
        return this.icon;
    }

    public List<Effect> getEffects(){
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
