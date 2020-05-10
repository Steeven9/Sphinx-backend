package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.misc.NotFoundException;
import ch.usi.inf.sa4.sphinx.misc.Operator;
import ch.usi.inf.sa4.sphinx.misc.StatusHolder;
import ch.usi.inf.sa4.sphinx.model.Automation;
import ch.usi.inf.sa4.sphinx.model.conditions.Condition;
import ch.usi.inf.sa4.sphinx.model.conditions.ConditionType;
import ch.usi.inf.sa4.sphinx.model.conditions.EqualityCondition;
import ch.usi.inf.sa4.sphinx.model.conditions.NumberCondition;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Optional;

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


    public Optional<Integer> createAutomation(@NonNull String username) {
        return userStorage.findByUsername(username)
                .map(user -> automationStorage.save(new Automation(user)).getId());
    }


    public Optional<List<Automation>> findByOwner(@NonNull String username) {
        return automationStorage.findByUserUsername(username);
    }

    public void addTrigger() {

    }

    public <T> void addCondition(Integer automationId, Condition<T> condition) {
        Automation automation = automationStorage.findById(automationId).orElseThrow(() -> new NotFoundException(""));



//        if (type.equals(ConditionType.EQUAL_TARGET)) {
//            try {
//                StatusHolder<? extends T> device = (StatusHolder<? extends T>) deviceStorage.findById(deviceId).orElseThrow(() -> new NotFoundException(""));
//                Condition condition = new EqualityCondition(device, target);
//                automation.addCondition(condition);
//            } catch (ClassCastException e) {
//                throw e;//TODO
//            }
//        }
//
//        if (type.equals(ConditionType.GREATER_TARGET) || type.equals(ConditionType.SMALLER_TARGET)) {
//            Operator operator;
//            if (type.equals(ConditionType.GREATER_TARGET)) {
//                operator = Operator.GREATER;
//            } else {
//                operator = Operator.SMALLER;
//            }
//
//            try {
//                StatusHolder<? extends Number> device = (StatusHolder<? extends Number>) deviceStorage.findById(deviceId).orElseThrow(() -> new NotFoundException(""));
//                Condition condition = new NumberCondition(device, operator, (Number) target);
//                automation.addCondition(condition);
//            } catch (ClassCastException e) {
//                throw e;//TODO
//            }
        }
        automationStorage.save(automation);

    }
}
