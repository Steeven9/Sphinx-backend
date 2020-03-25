package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Effect;

public interface EffectStorage {
    Effect get(int id);
    Integer insert(Effect c);
    void delete(int id);
    boolean update(Effect ev);
}
