package ch.usi.inf.sa4.sphinx.model;
import ch.usi.inf.sa4.sphinx.view.SerialisableRoom;

import java.util.*;



public class Room extends Storable<Integer, Room> {
	private String name;
	private String background;
	private String icon;
	private List<Integer> devices;



	public Room() {
		name = "Room";
		background = "./img/backgrounds/rooms/background-generic-room.svg";
		icon = "./img/icons/rooms/icon-generic-room.svg";
		devices = new ArrayList<>();
	}

	public Room(SerialisableRoom room) {
		this.name = room.name;
		this.icon = room.icon;
		this.background = room.background;
		this.devices = Arrays.asList(room.devices);
	}



	/**
	 * @return a copy of this object
	 */
	public Room makeCopy(){
		Room newRoom = new Room();
		newRoom.setKey(getKey());
		newRoom.name = this.name;
		newRoom.icon = this.icon;
		newRoom.background = this.background;
		newRoom.devices = new ArrayList<>(devices);
		return newRoom;
	}


	public boolean setId(Integer key) {
		return setKey(key);
	}

	public Integer getId(){
		return getKey();
	}

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
		sd.name = name;
		sd.id = getKey();
		return sd;

	}



}