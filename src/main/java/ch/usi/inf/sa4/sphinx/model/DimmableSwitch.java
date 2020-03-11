package ch.usi.inf.sa4.sphinx.model;

import java.lang.reflect.Array;
import java.util.List;
import java.util.function.Function;

public class DimmableSwitch extends Device {
    private double state;

    public String getLabel() {
        return Math.round(state*100) + "%";
    }

    public void setState(double newState) {
        state = newState;
        triggerObservers();
    }
    public double getState() {
        return state;
    }



}