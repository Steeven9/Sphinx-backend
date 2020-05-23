package ch.usi.inf.sa4.sphinx.model.scenesEffects;

public enum SceneTypes {
    LIGHT_INTENSITY,
    TEMPERATURE,
    POWER,
    CURTAINS_APERTURE;

    public int toInt(){
        switch (this){
            case LIGHT_INTENSITY:
                return 1;
            case TEMPERATURE:
                return 2;
            case POWER:
                return 3;
            case CURTAINS_APERTURE:
                return 4;
            default:
                return 0;
        }
    }
}
