package ch.usi.inf.sa4.sphinx.model;

import java.util.List;

public class Scene{
    private String name;
    private String icon;
    private List<Effect> effects;

    public Scene(List<Effect> effects, String name, String i){
        this.effects = effects;
        this.name = name;
        this.icon = i;
    }

    public String getName(){
        return this.name;
    }


    public String getIcon(){
        return this.icon;
    }

    public List<Effect> getEffects() {
        return this.effects;
    }

}
