package ch.usi.inf.sa4.sphinx.model;
import ch.usi.inf.sa4.sphinx.misc.NotImplementedException;

import java.util.*;



public class Room {
	private String name;
	private String background;
	private String icon;
	private final List<Integer> devices;
    private Integer id;


    //TODO
	/**
	 * @return a copy of this object
	 */
	public Room makeCopy(){
		throw new NotImplementedException();
	}

	public Room(){
		name = "Room";
		background = "/images/default_room";
		icon = "/images/default_icon";
		devices = new ArrayList<>();
	}

	public void setId(Integer id) {
		if(id==null) {
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
}