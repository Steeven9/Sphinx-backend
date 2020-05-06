package ch.usi.inf.sa4.sphinx.model;

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
    }

    public Scene makeCopy(Scene s){
        return new Scene(s);
    }

}
