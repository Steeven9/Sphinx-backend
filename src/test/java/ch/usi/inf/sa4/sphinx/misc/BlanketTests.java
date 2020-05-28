package ch.usi.inf.sa4.sphinx.misc;



import ch.usi.inf.sa4.sphinx.model.Coupling.BadCouplingException;
import ch.usi.inf.sa4.sphinx.model.Coupling.CouplingFactory;
import ch.usi.inf.sa4.sphinx.model.triggers.ConditionType;
import ch.usi.inf.sa4.sphinx.view.SerialisableEvent;
import org.junit.jupiter.api.DisplayName;
import ch.usi.inf.sa4.sphinx.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.api.Test;

public class BlanketTests {

    @Test
    void test1(){
        assertDoesNotThrow(()->{
           SerialisableEvent event =  new SerialisableEvent();
           event.setBooleanValue(true);
           event.setDoubleValue(0.5);
           event.setValue("true");
            event.setEventType(ConditionType.DEVICE_ON);
            event.getEventType();
            event.getDoubleValue();
            event.getSource();
            event.getType();
            event.getValue();
        });
    }
}
