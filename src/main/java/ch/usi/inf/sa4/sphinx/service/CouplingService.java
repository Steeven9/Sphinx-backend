package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Coupling;
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
}
