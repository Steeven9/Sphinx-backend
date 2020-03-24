package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Coupling;

import java.util.HashMap;
import java.util.Map;

public class VolatileCouplingStorage implements CouplingStorage {
    private static int id = 1;
    private static final Map<Integer, Coupling> couplings = new HashMap<>();


    private int makeId() {
        return id++;
    }

    /**
     * @param id of the coupling
     * @return the requested Coupling with given id
     */
    @Override
    public Coupling get(int id) {
        return couplings.get(id);
    }

    /**
     * @param c the coupling to insert in the Storage
     * @return the id of the inserted coupling
     */
    @Override
    public Integer insert(Coupling c) {
        Integer newId = makeId();
        if (c.setId(newId)) {
            couplings.put(newId, c.makeCopy());
            return c.getId();
        }
        return null;
    }

    /**
     * @param id id of the coupling to delete
     */
    @Override
    public void delete(int id) {
        couplings.remove(id);
    }
}
