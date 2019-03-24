package client;

import java.io.IOException;
import client.worker.Worker;
import utils.types.RequestType;

public class Client {
	public static void main(String args[]) throws IOException {
		RequestType type = RequestType.toRequestType(args[0]);
		String id = args[1];
		String serverAdd = args[2];
        int port = Integer.parseInt(args[3]);
        int noOfAccesses = Integer.parseInt(args[4]);
        Worker worker = type.generateWorker();
        if (worker != null) {
        	worker.execute(id, serverAdd, port, noOfAccesses);
        	worker.print_log(id);
        } else {
        	System.out.println("Invalid Request Type For Client!");
        }
    }        
}
