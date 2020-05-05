package ch.usi.inf.sa4.sphinx.model;

<<<<<<< HEAD
import ch.usi.inf.sa4.sphinx.view.SerialisableRoom;
import ch.usi.inf.sa4.sphinx.view.SerialisableScene;

import java.util.List;

public class Scene{
    private String name;
    private String icon;
    private List<Effect> effects;
    private User user;

    public Scene(List<Effect> effects, String name, String icon){
        this.effects = effects;
        this.name = name;
        this.icon = icon;
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
=======
import ch.usi.inf.sa4.sphinx.misc.DeviceType;

public class Scene extends Device{

    public Scene(){
        super();
    }

    @Override
    protected DeviceType getDeviceType() {
        return DeviceType.SCENE;
    }

    @Override
    public String getLabel() {
        return "Scene";
    }

    private Scene(Scene s){
        super.setIcon(s.getIcon());
        super.setName(s.getName());
        super.setOn(s.isOn());
    }

    public Scene makeCopy(Scene s){
        return new Scene(s);
    }

>>>>>>> #73: added set-up for Scene model, DeviceType modified to support Scene
}
