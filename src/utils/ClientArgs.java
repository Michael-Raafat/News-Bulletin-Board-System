package utils;

import utils.types.RequestType;

public class ClientArgs {
	RequestType type;
	String id;
	String serverAdd;
    int port;
    int noOfAccesses;
    
	public ClientArgs(RequestType type, String id, String serverAdd, int port, int noOfAccesses) {
		super();
		this.type = type;
		this.id = id;
		this.serverAdd = serverAdd;
		this.port = port;
		this.noOfAccesses = noOfAccesses;
	}
	
	public String toString() {
		return type.toString() + " " + id + " " + serverAdd + " " + port + " " + noOfAccesses;
	}
}
