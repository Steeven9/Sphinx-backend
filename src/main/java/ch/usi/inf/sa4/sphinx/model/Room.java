package ch.usi.inf.sa4.sphinx.model;
import java.util.*;

import ch.usi.inf.sa4.sphinx.misc.NotImplementedException;

import java.util.List;

public class Room {
	private String name;
	private String background;
	private String icon;
	private List<Integer> devices;
    private int id;

	public Room(){
		name= "Room"; // + id (?)
		background = "/images/default_room";
		icon = "/images/default_icon";
		devices = new ArrayList<>();
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
}