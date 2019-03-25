package client.rmi;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;

import server.rmi.RemoteAccessController;
import utils.log.Log;
import utils.log.record.ReadRecord;
import utils.log.record.WriteRecord;
import utils.types.RequestType;

public class RMIClient {
	private static final int MAX_SLEEP_TIME = 10000;
	public static void main(String args[]) throws IOException {
		Random rand = new Random();
		Log log = new Log();
        try {
        	RequestType type = RequestType.toRequestType(args[0]);
    		Integer id = Integer.parseInt(args[1]);
    		String serverAdd = args[2];
            int port = Integer.parseInt(args[3]);
            int noOfAccesses = Integer.parseInt(args[4]);
            if (type == null) {
            	System.out.println("Invalid request type!");
            	return;
            }
            System.out.println("Starting client !");
            Registry reg = LocateRegistry.getRegistry(serverAdd, port);
			RemoteAccessController controller = (RemoteAccessController) reg.lookup("RemoteAccessController");
			System.out.println("Running client !");
			for (int i = 0; i < noOfAccesses; i++) {
				try {
					Thread.sleep(rand.nextInt(MAX_SLEEP_TIME));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				String ret = controller.executeTask(id, type.toString(), id);
				if (type == RequestType.READ) {
					ReadRecord rec = new ReadRecord(ret);
					log.addToLog(rec);
				} else if (type == RequestType.WRITE) {
					WriteRecord rec = new WriteRecord(ret);
					log.addToLog(rec);
				}
			}
			if (type == RequestType.READ) {
				log.writeReaderLog(String.valueOf(id));
			} else if (type == RequestType.WRITE) {
				log.writeWriterLog(String.valueOf(id));
			}
			System.out.println("Terminating client !");
		} catch (NotBoundException e) {
			e.printStackTrace();
			System.out.println("Error !");
		}
    }       
}
