package ch.usi.inf.sa4.sphinx.model.Coupling;

import ch.usi.inf.sa4.sphinx.model.*;

import java.util.Optional;

public class CouplingFactory {
    public static Optional<Coupling> make(Device d1, Device d2) {
        return make(d1, d2, true);
    }

    private static Optional<Coupling> make(Device d1, Device d2, boolean first) {
        try {
            if (d1 instanceof Switch) {
                return Optional.of(new SwitchToDevice((Switch) d1, d2));
            }

            if (d1 instanceof StatelessDimmableSwitch) {
                return Optional.of(new StatelessDimSwitchToDimLight((StatelessDimmableSwitch) d1, (DimmableLight) d2));
            }

            if (d1 instanceof DimmableSwitch) {
                return Optional.of(new DimmSwitchToDimmLight((DimmableSwitch) d1, (DimmableLight) d2));
            }

            if (first) {
                return make(d2, d1, false);
            } else {
                return Optional.empty();
            }
        } catch (ClassCastException e) {
            return Optional.empty();

        }

    }

}
