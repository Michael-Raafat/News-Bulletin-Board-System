package utils.types;

import client.socket.worker.ReadWorker;
import client.socket.worker.Worker;
import client.socket.worker.WriteWorker;

public enum RequestType {
	READ, WRITE;
	
	public static RequestType toRequestType(String s) {
		if (s.equals("R")) {
			return READ;
		} else if (s.equals("W")) {
			return WRITE;
		}
		return null;
	}
	
	public Worker generateWorker() {
		switch(this) {
			case READ:
				return new ReadWorker();
			case WRITE:
				return new WriteWorker();
			default:
				return null;
		}
	}
	
	public String toString() {
		switch(this) {
			case READ:
				return "R";
			case WRITE:
				return "W";
			default:
				return "A";
		}
	}
}
