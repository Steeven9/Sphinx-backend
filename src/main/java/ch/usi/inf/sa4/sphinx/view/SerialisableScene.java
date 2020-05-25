package ch.usi.inf.sa4.sphinx.view;

import ch.usi.inf.sa4.sphinx.model.Effect;
import ch.usi.inf.sa4.sphinx.model.sceneEffects.Scene;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Represents the serialised version of a Scene entity
 * @see Scene
 */
public class SerialisableScene {
    private Integer id;
    private String name;
    private String icon;
    private boolean shared;
    @ApiModelProperty(notes = "ids of the devices owned by the scene")
    private List<SerialisableSceneEffect> effects;


    public SerialisableScene(Integer id, String name, String icon, List<SerialisableSceneEffect> effects, boolean shared) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.shared = shared;
        this.effects = effects;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public boolean isShared() {
        return shared;
    }

    public List<SerialisableSceneEffect> getEffects() {
        return effects;
    }

    /** Constructor.**/
    public SerialisableScene(){}
}