package ch.usi.inf.sa4.sphinx.view;

import io.swagger.annotations.ApiModelProperty;

/**
 * Represents the serialised version of a Room entity
 * @see ch.usi.inf.sa4.sphinx.model.Room
 */
public class SerialisableRoom {
    private Integer id;
    private String name;
    private String icon;
    private String background;
    @ApiModelProperty(notes = "ids of the devices owned by the room")
    private Integer[] devices;

    /** Getter for the room id.
     * @return the id of the room
     * **/
    public Integer getId(){
        return id;
    }
    /** Getter for the room name.
     * @return the name of the room
     * **/
    public String getName(){
        return name;
    }

    /** Getter for the room icon.
     * @return the icon of the room
     * **/
    public String getIcon(){
        return icon;
    }

    /** Getter for the room background.
     * @return the background of the room
     * **/
    public String getBackground(){
        return background;
    }

    /** Getter for the devices in a room.
     * @return the devices in the room
     * **/
    public Integer[] getDevices(){
        return devices;
    }

    /** Setter for the room id.
     * @param newName the new name of the room
     * **/
    public void setName(String newName){
        name = newName;
    }

    /** Setter for the room icon.
     * @param newIcon the new icon of the room
     * **/
    public void setIcon(String newIcon){
        icon = newIcon;
    }

    /** Setter for the room background.
     * @param newBackground the new background of the room
     * **/
    public void setBackground(String newBackground){
        background = newBackground;
    }

    /** Setter for the user id.
     * @param newId the user's new id
     * **/
    public void setId(Integer newId){
        id = newId;
    }

    /** Setter for the  ids of a user's device.
     * @param newDeviceIds the new id of the device
     **/
    public void setDeviceIds(Integer[] newDeviceIds){
        devices = newDeviceIds;
    }





}
