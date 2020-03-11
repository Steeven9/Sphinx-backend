package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.controller.Storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Coupling<T> {
    private final Event<T> event;
    private final List<Effect<T>> effects = new ArrayList<>();

    @SafeVarargs
    public Coupling(Event<T> event, Effect<T> ...effects) {
        this.event = event;
        this.effects.addAll(Arrays.asList(effects));
        Storage.getDevice(event.device).addObserver(() -> {for (Effect<T> effect : effects) effect.execute(event.get());});
    }
}
