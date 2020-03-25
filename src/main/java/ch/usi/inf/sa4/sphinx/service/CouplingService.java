package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Coupling;
import ch.usi.inf.sa4.sphinx.model.Effect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CouplingService {

    @Autowired
    private CouplingStorage couplingStorage;

    public Coupling get(int id) {
        return couplingStorage.get(id);
    }

    public Integer insert(Coupling c) {
        return couplingStorage.insert(c);
    }

    public void delete(int id) {
        couplingStorage.delete(id);
    }

    public boolean addEffect(int id, Effect effect){
        Coupling c = get(id);
        if (c == null) {
            return false;
        }
        return true;
    }
}
