package chat;


import java.io.Serializable;

/*
* Simple class to represent the IP/port/name information of a host
*/
public class NodeInfo implements Serializable {
	// declare the variables
	String address;
	int port = 0;
	String name = null;
	boolean hasJoined = false;

	/**
	 * Detail constructor
	 *
	 * Parameters: address, port, name
	 */
	public NodeInfo(String address, int port, String name, boolean hasJoined) {
		this.address = address;
		this.port = port;
		this.name = name;
		this.hasJoined = hasJoined;
	}

	/**
	 * Constructor when the name is null
	 *
	 * Parameters: address, port
	 */
	public NodeInfo(String address, int port) {
		this(address, port, null, false);
	}

	// getter methods
	String getAddress() {
		return this.address;
	}

	int getPort() {
		return this.port;
	}

	String getName() {
		return this.name;
	}
	
	boolean getJoined() {
		return this.hasJoined;
	}
	
	void setJoined(boolean joinedStatus) {
		this.hasJoined = joinedStatus;
	}
}