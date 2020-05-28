package ch.usi.inf.sa4.sphinx.view;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.annotations.ApiModelProperty;

/**
 * Represents the serialised version of a Device entity
 * @see ch.usi.inf.sa4.sphinx.model.Device
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SerialisableDevice {
    private Integer id;
    private String icon;
    private String name;
    private String label;
    @ApiModelProperty(notes = "ids of the devices this device is switched by")
    private int[] switched;
    @ApiModelProperty(notes = "ids of the devices this device switches")
    private int[] switches;
    @ApiModelProperty(notes = "position of the slider possibly associated with this device")
    private Double slider;
    @ApiModelProperty(notes = "id of the room owning this device")
    private Integer roomId;
    @ApiModelProperty(notes = "name of the room owning this device")
    private String roomName;
    @ApiModelProperty(notes = "type of this device ex: 0=LIGHT")
    private Integer type;
    @ApiModelProperty(notes = "name of the user owning this device")
    private String userName;
    private Boolean on;
    private double averageTemp;
    private Integer state;
    private Integer source;
    private Double tolerance;
    private Double quantity;
    private String video;


    /** Getter for the device id.
     * @return the device's id
     * **/
    public Integer getId(){
        return id;
    }

    /** Getter for the device icon.
     * @return the device's icon
     * **/
    public String getIcon(){
        return icon;
    }

    /** Getter for the device name.
     * @return the device's name
     * **/
    public String getName(){
        return name;
    }

    /** Getter for the device label.
     * @return the device's label
     * **/
    public String getLabel(){
        return label;
    }

    /** Getter for the devices this devices is switched by.
     * @return the ids of the devices this device is switched by
     * **/
    public int[] getSwitched(){
        return switched;
    }

    /** Getter for the devices this device switches.
     * @return the device's this device switches
     * **/
    public int[] getSwitches(){
        return switches;
    }

    /** Getter for the device slider value.
     * @return the device's slider value
     * **/
    public Double getSlider(){
        return slider;
    }

    /** Getter for the room id this device is in.
     * @return the id of the room this device is in
     * **/
    public Integer getRoomId(){
        return roomId;
    }

    /** Getter for the device id.
     * @return the device's id
     * **/
    public String getRoomName(){
        return roomName;
    }

    /** Getter for the device type.
     * @return the device's type
     * **/
    public Integer getType(){
        return type;
    }

    /** Getter for the username of the device's owner.
     * @return the username of the devices owner
     * **/
    public String getUserName(){
        return userName;
    }

    /** Getter for the device on state.
     * @return the device's on state
     * **/
    public Boolean getOnState(){
        return on;
    }

    /** Getter for the device id.
     * @return the device's id
     * **/
    public double getAverageTemp(){
        return averageTemp;
    }

    /** Getter for the device state.
     * @return the device's state
     * **/
    public Integer getState(){
        return state;
    }

    /** Getter for the device source.
     * @return the device's source
     * **/
    public Integer getSource(){
        return source;
    }

    /** Getter for the device tolerance.
     * @return the device's tolerance
     * **/
    public Double getTolerance(){
        return tolerance;
    }

    /** Getter for the device quantity.
     * @return the device's quantity
     * **/
    public Double getQuantity(){
        return quantity;
    }

    /** Getter for the device video file.
     * @return the device's video file
     * **/
    public String getVideo(){
        return video;
    }

    /** Setter for the device icon.
     * @param  newIcon the device's new icon
     * **/
    public void setIcon(String newIcon){
        icon = newIcon;
    }

    /** Setter for the device name.
     * @param  newName the device's new name
     * **/
    public void setName(String newName){
        name = newName;
    }

    /** Setter for the device label.
     * @param  newLabel the device's new label
     * **/
    public void setLabel(String newLabel){
        label = newLabel;
    }

    /** Setter for the device slider value.
     * @param  newSlider the device's new slider value
     * **/
    public void setSlider(Double newSlider){
        slider = newSlider;
    }

    /** Setter for the device's room id.
     * @param  newRoomId the device's room new id
     * **/
    public void setRoomId(Integer newRoomId){
        roomId = newRoomId;
    }

    /** Setter for the device type.
     * @param  newType the device's new type
     * **/
    public void setType(Integer newType){
        type = newType;
    }

    /** Setter for the device on state.
     * @param  newState the device's new on state
     * **/
    public void setOnState(Boolean newState){
        on = newState;
    }

    /** Setter for the device state.
     * @param  newState the device's new state
     * **/
    public void setState(Integer newState){
        state = newState;
    }

    /** Setter for the device source.
     * @param  newSource the device's new source
     * **/
    public void setSource(Integer newSource){
        source = newSource;
    }

    /** Setter for the device tolerance.
     * @param  newTolerance the device's new tolerance
     * **/
    public void setTolerance(Double newTolerance){
        tolerance = newTolerance;
    }

    /** Setter for the device quantity.
     * @param  newQuantity the device's new quantity
     * **/
    public void setQuantity(Double newQuantity){
        quantity = newQuantity;
    }

    /** Setter for the ids of the devices that this device is switched by.
     * @param  newSwitchedIds the device's new id
     * **/
    public void setSwitchedIds(int[] newSwitchedIds){
        switched = newSwitchedIds;
    }

    /** Setter for the ids of the devices that this device switches.
     * @param  newSwitchesIds the device's new id
     * **/
    public void setSwitchesIds(int[] newSwitchesIds){
        switches = newSwitchesIds;
    }

    /** Setter for the device's room name.
     * @param  newName the device's room new name
     * **/
    public void setRoomName(String newName){
        roomName = newName;
    }

    /** Setter for the device' user username.
     * @param  newUsername the device's user new username
     * **/
    public void setUserName(String newUsername){
        userName = newUsername;
    }

    /** Setter for the device average temperature.
     * @param  newAverageTemp the device's new average temperature
     * **/
    public void setAverageTemp(double newAverageTemp){
        averageTemp = newAverageTemp;
    }


    /** Setter for the device id.
     * @param  newId the device's new id
     * **/
    public void setId(Integer newId){
        id = newId;
    }


    /** Setter for the device video file.
     * @param  newVideoFile the device's new video file
     * **/
    public void setVideo(String newVideoFile){
        video = newVideoFile;
    }




}
