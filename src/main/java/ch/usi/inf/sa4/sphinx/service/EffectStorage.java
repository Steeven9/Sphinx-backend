package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Effect;

public interface EffectStorage {
    /** Getter for effects from storage.
     * @param id the id of the effect to get
     **/
    Effect get(Integer id);

    /** Insert effect to storage.
     * @param c effect to be inserted
     * @return the id of the inserted effect
     **/
    Integer insert(Effect c);

    /** Delete effect from storage.
     * @param id the id of the effect to be deleted
     **/
    void delete(Integer id);

    /** Update effect in storage.
     * @param ev the effect to be updated
     * @return true if successfully updated, false otherwise
     **/
    boolean update(Effect ev);
}
