package utils;

import utils.types.RequestType;

public class ClientArgs {
	RequestType type;
	String id;
	String serverAdd;
    int port;
    int noOfAccesses;
    boolean rmi;
    
	public ClientArgs(RequestType type, String id, String serverAdd, int port, int noOfAccesses, boolean rmi) {
		super();
		this.type = type;
		this.id = id;
		this.serverAdd = serverAdd;
		this.port = port;
		this.noOfAccesses = noOfAccesses;
		this.rmi = rmi;
	}
	
	public String toString() {
		return type.toString() + " " + id + " " + serverAdd + " " + port + " " + noOfAccesses;
	}
}
