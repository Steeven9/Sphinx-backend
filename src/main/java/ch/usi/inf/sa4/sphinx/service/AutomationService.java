package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.misc.NotFoundException;
import ch.usi.inf.sa4.sphinx.model.Automation;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Scene;
import ch.usi.inf.sa4.sphinx.model.conditions.Condition;
import ch.usi.inf.sa4.sphinx.model.conditions.ConditionFactory;
import ch.usi.inf.sa4.sphinx.model.triggers.Trigger;
import ch.usi.inf.sa4.sphinx.model.triggers.TriggerFactory;
import ch.usi.inf.sa4.sphinx.model.triggers.EventType;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
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
//    @Autowired
//    private EventStorage eventStorage;
    @Qualifier("triggerStorage")
    @Autowired
    private TriggerStorage triggerStorage;


    public Optional<Automation> createAutomation(@NonNull String username) {
        return userStorage.findByUsername(username)
                .map(user -> automationStorage.save(new Automation(user)));
    }


    public Optional<List<Automation>> findByOwner(@NonNull String username) {
        return automationStorage.findByUserUsername(username);
    }

    public void addTrigger(Integer automationId, Integer deviceId, EventType type, Object target) {
        Automation automation = automationStorage.findById(automationId).orElseThrow(()->new NotFoundException(""));
        Device device = deviceStorage.findById(deviceId).orElseThrow(()->new NotFoundException(""));
        Trigger trigger = TriggerFactory.makeEvent(device, target, type, automation);
        triggerStorage.save(trigger);
    }

    public void addCondition(Integer automationId, Integer deviceId, EventType type,  Object target){
        Automation automation = automationStorage.findById(automationId).orElseThrow(()->new NotFoundException(""));
        Device device = deviceStorage.findById(deviceId).orElseThrow(()->new NotFoundException(""));
        Condition condition = ConditionFactory.make(device, target, type);
        automation.addCondition(condition);
        automationStorage.save(automation);

    }

    public void addScene(Integer automationId, Integer sceneId){
        Automation automation = automationStorage.findById(automationId).orElseThrow(()->new NotFoundException(""));
        Scene scene = sceneStorage.findById(sceneId).orElseThrow(()->new NotFoundException(""));
        automation.addScene(scene);
        automationStorage.save(automation);
    }


    public void removeScene(Integer automationId, Integer sceneId){
        Automation automation = automationStorage.findById(automationId).orElseThrow(()->new NotFoundException(""));
        Scene scene = sceneStorage.findById(sceneId).orElseThrow(()->new NotFoundException(""));
        automation.removeScene(scene);
        automationStorage.save(automation);

    }


    public Optional<Automation> findById(Integer automationId ){
        return automationStorage.findById(automationId);
    }





//    public <T> void addCondition(Integer automationId, Condition<T> condition) {
//        Automation automation = automationStorage.findById(automationId).orElseThrow(() -> new NotFoundException(""));
//
//
//
////        if (type.equals(ConditionType.EQUAL_TARGET)) {
////            try {
////                StatusHolder<? extends T> device = (StatusHolder<? extends T>) deviceStorage.findById(deviceId).orElseThrow(() -> new NotFoundException(""));
////                Condition condition = new EqualityCondition(device, target);
////                automation.addCondition(condition);
////            } catch (ClassCastException e) {
////                throw e;//TODO
////            }
////        }
////
////        if (type.equals(ConditionType.GREATER_TARGET) || type.equals(ConditionType.SMALLER_TARGET)) {
////            Operator operator;
////            if (type.equals(ConditionType.GREATER_TARGET)) {
////                operator = Operator.GREATER;
////            } else {
////                operator = Operator.SMALLER;
////            }
////
////            try {
////                StatusHolder<? extends Number> device = (StatusHolder<? extends Number>) deviceStorage.findById(deviceId).orElseThrow(() -> new NotFoundException(""));
////                Condition condition = new NumberCondition(device, operator, (Number) target);
////                automation.addCondition(condition);
////            } catch (ClassCastException e) {
////                throw e;//TODO
////            }
//        }
//        automationStorage.save(automation);
//
//    }
}
