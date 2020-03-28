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

	public Room(){
		name = "Room";
		background = "/images/default_room";
		icon = "/images/default_icon";
		devices = new ArrayList<>();
	}

	public Room(SerialisableRoom room) {
		this.name = room.name;
		this.icon = room.icon;
		this.background = room.background;
		this.devices = Arrays.asList(room.devices);
	}


	//TODO move on super and delete getId and setId
	@Override
	public boolean setKey(Integer key) {
		return setId(key);
	}


	@Override
	public Integer getKey() {
		return getId();
	}

	public boolean setId(Integer id) {
		if (this.id == null) {
			this.id = id;
			return true;
		}
		return false;
	}

	public Integer getId(){ return id;}

	//-------- getter and setter for name ----------------------
	public String getName(){
		return this.name;
	}

	public void setName(String newName){
		this.name = newName;
	}

	//-------- getter and setter for background -----------------
	public String getBackground(){
		return this.background;
	}

	public void setBackground(String newBg){
		this.background = newBg;
	}

	//--------- getter and setter for icon ----------------------
	public String getIcon(){
		return this.icon;
	}

	public void setIcon(String newIcon){
		this.icon = newIcon;
	}

	//---------- getter for devices ----------------
	public List<Integer> getDevices(){
		return Collections.unmodifiableList(devices);
	}

	public void addDevice(Integer device){
		devices.add(device);
	}
	
	public void removeDevice(Integer device){
		devices.remove(device);

	}

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