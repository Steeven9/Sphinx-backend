package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Effect;
import org.springframework.stereotype.Repository;

@Repository
public class VolatileEffectStorage extends VolatileIntegerKeyStorage<Effect<?>> {
    private VolatileEffectStorage() {}
}
