package ch.usi.inf.sa4.sphinx.model;


/**
 * A StatelessDimmableSwitch is a dimmable switch that can change the intensity level of
 * a dimmable light, without storing the intensity level.
 */
public class StatelessDimmableSwitch extends Device {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return this.name + " " + this.on;
    }
}
