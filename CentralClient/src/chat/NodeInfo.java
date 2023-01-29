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

	/**
	 * Detail constructor
	 *
	 * Parameters: address, port, name
	 */
	public NodeInfo(String address, int port, String name) {
		this.address = address;
		this.port = port;
		this.name = name;
	}

	/**
	 * Constructor when the name is null
	 *
	 * Parameters: address, port
	 */
	public NodeInfo(String address, int port) {
		this(address, port, null);
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
}