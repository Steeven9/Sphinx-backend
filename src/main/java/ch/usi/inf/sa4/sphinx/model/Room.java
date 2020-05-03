package ch.usi.inf.sa4.sphinx.model;
import ch.usi.inf.sa4.sphinx.view.SerialisableRoom;
import com.google.gson.annotations.Expose;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a Room of a house.
 * The Room can contain Device(s)
 * @see Device
 */
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
	@ManyToOne(fetch = FetchType.EAGER, optional = false) //TODO check why merge cascade type
	@JoinColumn(name = "user_id", nullable = false)
	private User user;


	/**
	 * Creates a room with default name,background,icon and no devices.
	 */
	public Room(){
		name = "Room";
		background = "./img/backgrounds/rooms/background-generic-room.svg";
		icon = "./img/icons/rooms/icon-generic-room.svg";
		devices = new ArrayList<>();
	}


	/**
	 * Used to set the onwer of the Room.
	 * @param user the owner of this Room
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Creates a Room given the parameters in a serialised version of it
	 * @param room a serialised version of a Room
	 */
	public Room(SerialisableRoom room) {
		this();
		if(room.name != null) this.name = room.name;
		if(room.icon != null) this.icon = room.icon;
		if(room.background != null) this.background = room.background;
	}


	/**
	 * Adds a Device to this Room.
	 * @param device the Device to add
	 */
	public void addDevice(Device device){
		if (device == null){
			throw new IllegalArgumentException("device can not be null");
		}
		device.setRoom(this);
		devices.add(device);
	}


	/**
	 * @return a list of the Device(s) in this Room
	 * @see Device
	 */
	public List<Device> getDevices() {
		return devices;
	}


	/**
	 * @return the User that owns this Room
	 * @see User
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @return the name of this Room
	 */
	public String getName(){
		return this.name;
	}

	/**
	 * sets a new name.
	 * @param newName the name to give this Room
	 */
	public void setName(String newName){
		this.name = newName;
	}

	/**
	 * @return the background for this Room
	 */
	public String getBackground(){
		return this.background;
	}

	/**
	 * Sets a new background.
	 * @param newBg the ne background for this Room
	 */
	public void setBackground(String newBg){
		this.background = newBg;
	}

	/**
	 * @return the icon of this Room
	 */
	public String getIcon(){
		return this.icon;
	}

	/**
	 * Sets a new icon
	 * @param newIcon  the new icnon to set
	 */
	public void setIcon(String newIcon){
		this.icon = newIcon;
	}

	/**
	 * @return A list of the ids of the Devices in this Room
	 */
	public List<Integer> getDevicesIds(){
		return devices.stream().map(Device::getId).collect(Collectors.toList());
	}

	/**
	 * Removes a Device from this Room.
	 * Notice that calling this method alone WON'T alter the corresponding Room saved in storage,
	 * to remove a device call roomService.removeDevice(...)
	 * @param deviceId the id of the Device to remove from this Room
	 * @see Device
	 */
	public void removeDevice(Integer deviceId) {
		devices.removeIf(device -> device.getId().equals(deviceId));
	}

	/**
	 * @return a serialised version of this Room
	 */
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

