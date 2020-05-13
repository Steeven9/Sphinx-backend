package ch.usi.inf.sa4.sphinx.view;


import java.util.ArrayList;
import java.util.List;

public class SerialisableAutomation {
    private Integer id;
    private String name;
    private String icon;
    private Integer ownerId;
    private List<Integer> scenes;
    private List<SerialisableCondition> triggers;
    private List<SerialisableCondition> conditions;



    public SerialisableAutomation(Integer id, String name, String icon, Integer ownerId, List<SerialisableCondition> triggers) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.ownerId = ownerId;
        this.triggers = triggers;
    }

    public SerialisableAutomation(){
    }


    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public Integer getOwnerId() {
        return ownerId;
    }


    public List<Integer> getScenes() {
        return scenes;
    }

    public List<SerialisableCondition> getConditions() {
        return conditions;
    }

    public List<SerialisableCondition> getTriggers() {
        return triggers;
    }


}
