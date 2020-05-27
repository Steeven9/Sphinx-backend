package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.misc.NotFoundException;
import ch.usi.inf.sa4.sphinx.model.Automation;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.conditions.Condition;
import ch.usi.inf.sa4.sphinx.model.conditions.ConditionFactory;
import ch.usi.inf.sa4.sphinx.model.sceneEffects.Scene;
import ch.usi.inf.sa4.sphinx.model.triggers.ConditionType;
import ch.usi.inf.sa4.sphinx.model.triggers.Trigger;
import ch.usi.inf.sa4.sphinx.model.triggers.TriggerFactory;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AutomationService {

    @Autowired
    private DeviceStorage deviceStorage;
    @Autowired
    private AutomationStorage automationStorage;
    @Autowired
    private SceneStorage sceneStorage;
    @Qualifier("userStorage")
    @Autowired
    private UserStorage userStorage;
    @Qualifier("triggerStorage")
    @Autowired
    private TriggerStorage triggerStorage;
    @Qualifier("conditionStorage")
    @Autowired
    private ConditionStorage conditionStorage;


    public Optional<Automation> createAutomation(@NonNull String username) {
        return userStorage.findByUsername(username)
                .map(user -> automationStorage.save(new Automation(user)));
    }


    public Optional<List<Automation>> findByOwner(@NonNull String username) {
        return automationStorage.findByUserUsername(username);
    }

    public void addTrigger(Integer automationId, Integer deviceId, ConditionType type, Object target) {
        Automation automation = automationStorage.findById(automationId).orElseThrow(() -> new NotFoundException(""));
        Device device = deviceStorage.findById(deviceId).orElseThrow(() -> new NotFoundException(""));
        Trigger trigger = TriggerFactory.makeEvent(device, target, type, automation);
        triggerStorage.save(trigger);
    }

    public void addCondition(Integer automationId, Integer deviceId, ConditionType type, Object target) {
        Automation automation = automationStorage.findById(automationId).orElseThrow(() -> new NotFoundException(""));
        Device device = deviceStorage.findById(deviceId).orElseThrow(() -> new NotFoundException(""));
        Condition condition = ConditionFactory.make(device, target, type);
        automation.addCondition(condition);
        automationStorage.save(automation);

    }

    public void addScene(int automationId, int sceneId) {
        Automation automation = automationStorage.findById(automationId).orElseThrow(() -> new NotFoundException(""));
        Scene scene = sceneStorage.findById(sceneId).orElseThrow(() -> new NotFoundException(""));
        automation.addScene(scene);
        automationStorage.save(automation);
    }


    /**
     * Adds the scenes with the specified ids to the automation but
     * only if both belong to the User with the specified username.
     * If a Scene does not exist or does not belong to the User it throws a NotFoundException.
     *
     * @param username     name of the USer
     * @param automationId id of the Automation
     * @param sceneIds     id of the scenes
     */
    public void addScenesIfBelongsTo(String username, int automationId, List<Integer> sceneIds) {
        Automation automation = automationStorage.findById(automationId).orElseThrow(() -> new NotFoundException(""));
        if (!automation.getUser().getUsername().equals(username)) throw new NotFoundException("");
        sceneIds.forEach(id -> {
            sceneStorage.findById(id).ifPresentOrElse(scene -> {
                        if (!scene.getUser().getUsername().equals(username)) throw new NotFoundException("");
                        automation.addScene(scene);
                    }, () -> {
                        throw new NotFoundException("");
                    }
            );
        });
        automationStorage.save(automation);
    }

    public void removeScene(Integer automationId, Integer sceneId) {
        Automation automation = automationStorage.findById(automationId).orElseThrow(() -> new NotFoundException(""));
        Scene scene = sceneStorage.findById(sceneId).orElseThrow(() -> new NotFoundException(""));
        automation.removeScene(scene);
        automationStorage.save(automation);
    }


    public Optional<Automation> findById(Integer automationId) {
        return automationStorage.findById(automationId);
    }


    public void deleteAutomation(Integer automationId) {
        automationStorage.deleteById(automationId);
        triggerStorage.deleteByAutomationId(automationId);
    }

    public void removeConditions(int automationId) {
        automationStorage.findById(automationId).ifPresent(automation -> {
            automation.getConditions().stream()
                    .map(Condition::getId)
                    .forEach(automationStorage::deleteById);
        });
    }

    public void removeTriggers(int automationId) {
        triggerStorage.deleteByAutomationId(automationId);

    }


    public void deleteAllByUser(String username){

    }


    public List<Trigger> findTriggers(int automationId) {
        return triggerStorage.findByAutomationId(automationId);
    }
}
