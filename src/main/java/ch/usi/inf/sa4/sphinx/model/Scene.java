package ch.usi.inf.sa4.sphinx.model;

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

}
