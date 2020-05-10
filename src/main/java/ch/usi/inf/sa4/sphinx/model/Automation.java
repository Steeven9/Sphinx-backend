package ch.usi.inf.sa4.sphinx.model;


import ch.usi.inf.sa4.sphinx.model.conditions.Condition;
import lombok.NonNull;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Automation extends StorableE implements Runnable{
    @OneToMany(cascade = CascadeType.ALL)
    private List<Coupling> triggers;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Condition> conditions;
    @OneToMany
    private Set<Scene> scenes;
    private String name;
    private User user;


    public String getName() {
        return name;
    }

    /**
     * Adds an event to the list of those that will trigger this automation
     * @param event the event that will trigger this automation
     */
    public void addTrigger(@NonNull Event<Object> event, Device device){
        Coupling newTrigger = new Coupling(event, new RunAutomation(this));
        device.addObserver(newTrigger);
        triggers.add(newTrigger);
    }


    public void addScene(@NonNull Scene scene){
        this.scenes.add(scene);
    }

    public void addCondition(@NonNull Condition condition){
        conditions.add(condition);
    }



    public Automation(User user) {
        this.triggers = new ArrayList<>();
        this.conditions = new ArrayList<>();
        this.scenes = new HashSet<>();
        this.user =user;
    }

    @Override
    public void run() {
        if(conditions.stream().allMatch(Condition::check)){
            scenes.forEach(Scene::run);
        }
    }
}
