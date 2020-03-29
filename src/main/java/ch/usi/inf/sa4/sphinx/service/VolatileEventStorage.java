package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Event;
import org.springframework.stereotype.Repository;


@Repository
public class VolatileEventStorage extends VolatileIntegerKeyStorage<Event<?>> {
    private VolatileEventStorage() {
    }
}
