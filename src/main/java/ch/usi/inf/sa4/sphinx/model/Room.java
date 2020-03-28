package ch.usi.inf.sa4.sphinx.model;
import ch.usi.inf.sa4.sphinx.view.SerialisableRoom;

import java.util.*;




public class Room extends Storable<Integer, Room> {
	private String name;
	private String background;
	private String icon;
	private List<Integer> devices;
    private Integer id;


	/**
	 * @return a copy of this object
	 */
	public Room makeCopy(){
		Room newRoom = new Room();
		newRoom.name = this.name;
		newRoom.icon = this.icon;
		newRoom.background = this.background;
		newRoom.id = this.id;
		newRoom.devices = new ArrayList<>(devices);
		return newRoom;
	}

	/** Constructor.**/
	public Room(){
		name = "Room";
		background = "/images/default_room";
		icon = "/images/default_icon";
		devices = new ArrayList<>();
	}

	/** Constructor.
	 * @param room a SerialisableRoom
	 **/
	public Room(SerialisableRoom room) {
		this.name = room.name;
		this.icon = room.icon;
		this.background = room.background;
		this.devices = Arrays.asList(room.devices);
	}


	/** Setter for the id of a room.
	 * @param id the id of the room
	 * @return true iff the operation was successful
	 **/
	public boolean setId(Integer id) {
		if (this.id == null) {
			this.id = id;
			return true;
		}
		return false;
	}

	/** Getter for id.
	 * @return the id
	 **/
	public Integer getId(){ return id;}

	//-------- getter and setter for name ----------------------

	/** Getter for the room name.
	 * @return the name of the room
	 **/
	public String getName(){
		return this.name;
	}

	/** Setter for the room name.
	 * @param newName  the name of the room
	 **/
	public void setName(String newName){
		this.name = newName;
	}

	//-------- getter and setter for background -----------------

	/** Getter for the room background.
	 * @return the background of the room
	 **/
	public String getBackground(){
		return this.background;
	}

	/** Setter for the room background.
	 * @param newBg  the background of the room
	 **/
	public void setBackground(String newBg){
		this.background = newBg;
	}

	//--------- getter and setter for icon ----------------------
	/** Getter for the room icon.
	 * @return the icon of the room
	 **/
	public String getIcon(){
		return this.icon;
	}

	/** Setter for the room icon.
	 * @param newIcon  the icon of the room
	 **/
	public void setIcon(String newIcon){
		this.icon = newIcon;
	}

	//---------- getter for devices ----------------
	/** Getter for the room devices.
	 * @return the list of devices of the room
	 **/
	public List<Integer> getDevices(){
		return Collections.unmodifiableList(devices);
	}

	/** Add device to a room.
	 * @param device  the device to add
	 **/
	public void addDevice(Integer device){
		devices.add(device);
	}

	/** Remove device to a room.
	 * @param device  the device to remove
	 **/
	public void removeDevice(Integer device){
		devices.remove(device);

	}

	/** Serialize a room.
	 * @return a SerialisableRoom
	 **/
	public SerialisableRoom serialise(){
		SerialisableRoom sd = new SerialisableRoom();
		sd.devices = devices.toArray(new Integer[0]);
		sd.background = background;
		sd.icon = icon;
		sd.id = id;
		sd.name = name;
		return sd;

	}



}