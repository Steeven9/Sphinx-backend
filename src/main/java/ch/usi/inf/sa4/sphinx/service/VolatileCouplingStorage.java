package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Coupling;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;


@Repository
public class VolatileCouplingStorage extends VolatileIntegerKeyStorage<Coupling>{
    private VolatileCouplingStorage(){}
}
