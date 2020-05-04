package ch.usi.inf.sa4.sphinx.view;

import io.swagger.annotations.ApiModelProperty;

/**
 * Represents the serialised version of a Room entity
 * @see ch.usi.inf.sa4.sphinx.model.Room
 */
public class SerialisableRoom {
    public Integer id;
    public String name;
    public String icon;
    public String background;
    @ApiModelProperty(notes = "ids of the devices owned by the room")
    public Integer[] devices;

    /** Constructor.**/
    public SerialisableRoom(){}
}
