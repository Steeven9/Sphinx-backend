package ch.usi.inf.sa4.sphinx.view;

import java.util.List;

public class SerialisableSceneEffect {
    private Integer id;
    private Integer type;
    private String name;
    private Double slider;
    private Boolean on;
    private List<Integer> devices;

    public SerialisableSceneEffect(Integer id, Integer type, String name, Double slider, Boolean on, List<Integer> devices) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.slider = slider;
        this.on = on;
        this.devices = devices;
    }

    public Integer getId() {
        return id;
    }

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Double getSlider() {
        return slider;
    }

    public Boolean getOn() {
        return on;
    }

    public List<Integer> getDevices() {
        return devices;
    }

    public Object getTarget(){
        return slider != null ? (Object) slider : (Object) on;
    }


}
