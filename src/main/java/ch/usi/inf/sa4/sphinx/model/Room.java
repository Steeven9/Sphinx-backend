package ch.usi.inf.sa4.sphinx.model;
import ch.usi.inf.sa4.sphinx.misc.NotImplementedException;
import ch.usi.inf.sa4.sphinx.view.SerialisableRoom;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.google.gson.annotations.Expose;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.persistence.Lob;
import java.util.*;
import java.util.stream.Collectors;


@Entity
public class Room extends StorableE{
	@Expose
	private String name;
	@Expose
	@Lob
	@Type(type = "org.hibernate.type.TextType")//TODO check later what this thing actually does
	private String background;
	@Expose
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String icon;
	@Expose
	@OneToMany(orphanRemoval = true, //A device can migrate Room
			cascade = CascadeType.ALL,
			mappedBy = "room",
			fetch = FetchType.LAZY)
	private List<Device> devices;
	//not all since otherwise it will try to persist the User
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;


	public Room(){
		name = "Room";
		background = "./img/backgrounds/rooms/background-generic-room.svg";
		icon = "./img/icons/rooms/icon-generic-room.svg";
		devices = new ArrayList<>();
	}





	public void setUser(User user) {
		this.user = user;
	}

	public Room(SerialisableRoom room) {
		this();
		if(room.name != null) this.name = room.name;
		if(room.icon != null) this.icon = room.icon;
		if(room.background != null) this.background = room.background;
	}


	public void addDevice(Device device){
		if (device == null){
			throw new IllegalArgumentException("device can not be null");
		}
		device.setRoom(this);
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
		return devices.stream().map(Device::getId).collect(Collectors.toList());
	}

	//Notice that calling this method alone WON'T alter the corresponding Room saved in storage,
	// to remove a device call roomService.removeDevice(...)
	public void removeDevice(Integer deviceId){
		devices.remove(deviceId);
	}

	public SerialisableRoom serialise(){
		SerialisableRoom sd = new SerialisableRoom();
		sd.devices = devices.stream().map(Device::getId).toArray(Integer[]::new);
		sd.background = background;
		sd.icon = icon;
		sd.name = name;
		sd.id = getId();
		return sd;

	}



}