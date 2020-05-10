package ch.usi.inf.sa4.sphinx.misc;

public enum Operator {
    GREATER,
    SMALLER,
    EQUAL;

    public boolean act(Number a, Number b) {
        switch (this) {
            case EQUAL:
                return a.doubleValue() == b.doubleValue();
            case GREATER:
                return a.doubleValue() > b.doubleValue();
            case SMALLER:
                return a.doubleValue() < b.doubleValue();

        }
        return false;
    }
}
