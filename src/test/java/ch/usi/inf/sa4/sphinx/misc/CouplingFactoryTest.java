package ch.usi.inf.sa4.sphinx.misc;


import ch.usi.inf.sa4.sphinx.model.Coupling.BadCouplingException;
import ch.usi.inf.sa4.sphinx.model.Coupling.CouplingFactory;
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
public class CouplingFactoryTest {

    @Test
    void test(){
        assertEquals(CouplingFactory.make(new DimmableLight(), new DimmableLight()), Optional.empty() );
    }


}
