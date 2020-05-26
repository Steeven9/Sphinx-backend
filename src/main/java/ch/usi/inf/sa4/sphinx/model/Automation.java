package ch.usi.inf.sa4.sphinx.model;


import ch.usi.inf.sa4.sphinx.misc.ServiceProvider;
import ch.usi.inf.sa4.sphinx.model.conditions.Condition;
import ch.usi.inf.sa4.sphinx.model.sceneEffects.Scene;
import ch.usi.inf.sa4.sphinx.model.triggers.Trigger;
import ch.usi.inf.sa4.sphinx.service.AutomationService;
import ch.usi.inf.sa4.sphinx.view.SerialisableAutomation;
import ch.usi.inf.sa4.sphinx.view.SerialisableCondition;
import lombok.NonNull;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Automation extends StorableE implements Runnable {
    //    @OneToMany(cascade = CascadeType.ALL)
//    private List<Coupling> triggers;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Condition> conditions;
    @OneToMany
    private Set<Scene> scenes;
    private String name;
    @ManyToOne
    private User user;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String icon;


    public String getName() {
        return name;
    }


    public void addScene(@NonNull Scene scene) {
        this.scenes.add(scene);
    }

    public void removeScene(@NonNull Scene scene) {
        this.scenes.remove(scene);
    }

    public void addCondition(@NonNull Condition condition) {
        conditions.add(condition);
    }


    public Automation(User user) {
        this.conditions = new ArrayList<>();
        this.scenes = new HashSet<>();
        this.user = user;
        this.name = "default";
    }

    @Override
    public void run() {
        if (conditions.stream().allMatch(Condition::check)) {
            scenes.forEach(Scene::run);
        }
    }

    public User getUser() {
        return user;
    }

    public Set<Scene> getScenes() {
        return scenes;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public SerialisableAutomation serialise() {
        List<Integer> sceneIds = scenes.stream().map(Scene::getId).collect(Collectors.toList());
        List<SerialisableCondition> serialisableConditions = conditions.stream()
                .map(Condition::serialise)
                .collect(Collectors.toList());

        AutomationService automationService = ServiceProvider.getAutomationService();
        List<SerialisableCondition> serialisableTriggers = automationService.findTriggers(id).stream()
                .map(Trigger::serialise).collect(Collectors.toList());

        return new SerialisableAutomation(id, name, icon, user.getId(), sceneIds, serialisableTriggers, serialisableConditions);
    }
}
