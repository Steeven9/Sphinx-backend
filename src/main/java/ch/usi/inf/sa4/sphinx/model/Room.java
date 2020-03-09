package ch.usi.inf.sa4.sphinx.model;
import java.util.ArrayList;

public class Room {
	protected String name;
	protected String background;
	protected String icon;
	protected ArrayList<Integer> devices;
	// protected int id;

	public Room(String name){
		this.name=name;
		background = "/images/default_room";
		icon = "/images/default_icon";
		devices = new ArrayList<>();
	}

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
	public ArrayList<Integer> getDevices(){
		return devices;
	}
}