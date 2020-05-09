package ch.usi.inf.sa4.sphinx.view;

import io.swagger.annotations.ApiModelProperty;

/**
 * Represents the serialised version of a Device entity
 * @see ch.usi.inf.sa4.sphinx.model.Device
 */
public class SerialisableDevice {
    public Integer id;
    public String icon;
    public String name;
    public String label;
    @ApiModelProperty(notes = "ids of the devices this device is switched by")
    public int[] switched;
    @ApiModelProperty(notes = "ids of the devices this device switches")
    public int[] switches;
    @ApiModelProperty(notes = "position of the slider possibly associated with this device")
    public Double slider;
    @ApiModelProperty(notes = "id of the room owning this device")
    public Integer roomId;
    @ApiModelProperty(notes = "name of the room owning this device")
    public String roomName;
    @ApiModelProperty(notes = "type of this device ex: 0=LIGHT")
    public Integer type;
    @ApiModelProperty(notes = "name of the user owning this device")
    public String userName;
    public Boolean on;
    public String url;
    public double averageTemp;
    public Integer state;
    public Integer source;

}
