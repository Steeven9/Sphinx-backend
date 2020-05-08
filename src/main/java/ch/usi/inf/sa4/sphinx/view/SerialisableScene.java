package ch.usi.inf.sa4.sphinx.view;

import ch.usi.inf.sa4.sphinx.model.Effect;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Represents the serialised version of a Scene entity
 * @see ch.usi.inf.sa4.sphinx.model.Scene
 */
public class SerialisableScene {
    public Integer id;
    public String name;
    public String icon;
    @ApiModelProperty(notes = "ids of the devices owned by the scene")
    public List<Effect> effects;

    /** Constructor.**/
    public SerialisableScene(){}
}