package ch.usi.inf.sa4.sphinx.model.sceneEffects;


// 1 (=Light intensity value=%)
// 2 (=Temperature value=double)
// 3 (=Power value=boolean)
// 4 (=Curtains aperture value=%)
public enum SceneType {
    LIGHT_INTENSITY,
    TEMPERATURE,
    POWER,
    CURTAINS_APERTURE;

    public int toInt() {
        switch (this) {
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


    public static SceneType intToType(int t) {
        switch (t) {
            case 1:
                return SceneType.LIGHT_INTENSITY;
            case 2:
                return SceneType.TEMPERATURE;
            case 3:
                return SceneType.POWER;
            case 4:
                return SceneType.CURTAINS_APERTURE;
            default:
                throw new IllegalArgumentException("");

        }
    }
}
