package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.NotImplementedException;

public class Light extends Device {
    private String type;

    public Light(String type) {
        super();
        this.type = type;
    }

    @Override
    public String getLabel() {
        throw new NotImplementedException();
    }
}