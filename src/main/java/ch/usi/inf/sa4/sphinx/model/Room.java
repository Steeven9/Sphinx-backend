package ch.usi.inf.sa4.sphinx.model;
import ch.usi.inf.sa4.sphinx.misc.NotImplementedException;
import ch.usi.inf.sa4.sphinx.view.SerialisableRoom;
import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.*;



@Entity
public class Room extends StorableE{
	@Expose
	private String name;
	@Expose
	private String background;
	@Expose
	private String icon;
	@Expose
	@OneToMany(orphanRemoval = false, //A device can migrate Room
			cascade = CascadeType.ALL,
			mappedBy = "room",
			fetch = FetchType.LAZY)
	private List<Device> devices;
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;


	public Room(){
		name = "Room";
		background = "/images/default_room";
		icon = "/images/default_icon";
		devices = new ArrayList<>();
	}


	public void setUser(User user) {
		this.user = user;
	}

	public Room(SerialisableRoom room) {
		this.name = room.name;
		this.icon = room.icon;
		this.background = room.background;
		//this.devices = Arrays.asList(room.devices);
	}


	public void addDevice(Device device){
		if (device == null){
			throw new IllegalArgumentException("device can not be null");
		}
		devices.add(device);
	}



	public List<Device> getDevices() {
		return devices;
	}



	public User getUser() {
		return user;
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
	public List<Integer> getDevicesIds(){
		throw  new NotImplementedException();
	}

	public void addDeviceTODELETE(Device device){
		throw  new NotImplementedException(); //TODO DELET DIS
	}

	//THIS WONT REMOVE THE DEVICE FROM STORAGE!
	public void removeDevice(Integer deviceId){
		devices.remove(deviceId);//TODO fix

	}

	public SerialisableRoom serialise(){
		SerialisableRoom sd = new SerialisableRoom();
		sd.devices = devices.toArray(new Integer[0]);
		sd.background = background;
		sd.icon = icon;
		sd.name = name;
		sd.id = getId();
		return sd;

	}



}