package ch.usi.inf.sa4.sphinx.model;

/**
 * A Light is a normal light that can be either on either off.
=======
import ch.usi.inf.sa4.sphinx.misc.NotImplementedException;

/**
 * A Light is a normal light that can be on or off.
>>>>>>> almost all subclasses implementd, except StatelessDimmableSwitch
 */
public class Light extends Device {

    /**
<<<<<<< HEAD
     * Creates a light.
     */
    public Light() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return this.on ? "on" : "off";
    }
}