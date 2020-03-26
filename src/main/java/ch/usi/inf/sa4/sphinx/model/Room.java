package ch.usi.inf.sa4.sphinx.model;
import ch.usi.inf.sa4.sphinx.misc.NotImplementedException;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import ch.usi.inf.sa4.sphinx.view.SerialisableRoom;

import java.util.*;



public class Room {
	private String name;
	private String background;
	private String icon;
	private final List<Integer> devices;
    private Integer id;


	/**
	 * @return a copy of this object
	 */
	public Room makeCopy(){
		return new Room(this);
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

	public Room(Room room) {
		this.name = room.name;
		this.icon = room.icon;
		this.background = room.background;
		this.devices = Collections.unmodifiableList(room.devices);
	}

	public void setId(Integer id) {
		if (this.id == null) {
			this.id = id;
		}
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

	public SerialisableRoom serialize(){
		SerialisableRoom sd = new SerialisableRoom();
		sd.devices = devices.toArray(new Integer[0]);
		sd.background = background;
		sd.icon = icon;
		sd.id = id;
		sd.name = name;
		return sd;

	}



}