package main;

import server.ServerWorker;
import utils.SSHConnection;
import utils.SystemConfiguration;

public class Start {
	private static final int securePort = 22;
	
	
	public static void main(String[] args) {
		SystemConfiguration c = new SystemConfiguration("system.properties.txt");
		if (c.validConfiguration()) {
			Thread t = createServer(c);
			if (t == null) {
				System.out.println("Failed to create server, terminating process !");
				System.exit(-1);
			}
			t.start();
			createClients(c);
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Invalid Configuration");
		}
		System.exit(0);
	}
	
	public static Thread createServer(SystemConfiguration c) {
		// Creating server thread.
		int requests = (c.getNumberOfReaders() + c.getNumberOfWriters()) * c.getNumberOfAccess();
		ServerWorker worker = null;
		try {
			worker = new ServerWorker(c.getServerPort(), requests);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			worker = null;
		}
		return worker;
	}
	
	public static void createClients(SystemConfiguration c) {
		// calling client.java to create readers and writers
		SSHConnection con = new SSHConnection();
		 try {
			 for (int i = 0; i < c.getNumberOfReaders(); i++) {
				 if (con.openConnection(c.getReadersAdd()[i], securePort, "amr", c.getReadersPass()[i], 120000)) {
	                    

	                    con.sendCommand("echo Client.java hello");
	                    System.out.println(con.recvData());
	                    Thread.sleep(300);
	                    con.closeConnection();
	                }
			 }
			 for (int i = 0; i < c.getNumberOfWriters(); i++) {
				 
			 }	             
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
		
}
