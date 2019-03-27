package main;

import java.util.ArrayList;
import java.util.List;

import com.jcraft.jsch.ChannelExec;

import server.rmi.RMIServerWorker;
import server.socket.ServerWorker;
import utils.ClientArgs;
import utils.SSHConnection;
import utils.SystemConfiguration;
import utils.types.RequestType;

public class Start {
	public static void main(String[] args) {
		boolean rmi = false;
		if (args.length > 0 && args[0].equals("rmi")) {
			System.out.println("Running in RMI mode");
			rmi = true;
		} else {
			System.out.println("Running in socket mode");
		}
		SystemConfiguration c = new SystemConfiguration("system.properties.txt", rmi);
		if (c.validConfiguration()) {
			Thread t = createServer(c);
			if (t == null) {
				System.out.println("Failed to create server, terminating process !");
				System.exit(-1);
			}
			t.start();
			List<ChannelExec> processes = createClients(c);
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Server finished serving all requests");
			for (int i = 0; i < processes.size(); i++) {
					processes.get(i).disconnect();
			}
			System.out.println("All clients terminated");
		} else {
			System.out.println("Invalid Configuration");
		}
		System.exit(0);
	}
	
	public static Thread createServer(SystemConfiguration c) {
		// Creating server thread.
		int requests = (c.getNumberOfReaders() + c.getNumberOfWriters()) * c.getNumberOfAccess();
		Thread worker = null;
		try {
			if (c.isRmi()) {
				worker = new RMIServerWorker(c.getServerPort(), c.getRmiPort(), requests);
			} else {
				worker = new ServerWorker(c.getServerPort(), requests);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			worker = null;
		}
		return worker;
	}
	
	public static List<ChannelExec> createClients(SystemConfiguration c) {
		// calling client.java to create readers and writers
		SSHConnection con = new SSHConnection();
		List<ChannelExec> processes = new ArrayList<ChannelExec>();
		 try {
			 for (int i = 0; i < c.getNumberOfReaders(); i++) {
				 int port;
				 if (c.isRmi()) {
					 port = c.getRmiPort();
				 } else {
					 port = c.getServerPort();
				 }
				 ClientArgs args = new ClientArgs(RequestType.READ,
						 String.valueOf(c.getReadersIDs()[i]), c.getServerAdd(), port, c.getNumberOfAccess(), c.isRmi());
				 System.out.println("Creating reader process " + i);
				 if (con.openConnection(c.getReadersAdd()[i], c.getReadersPass()[i], c.getReadersUsername()[i], args, c.getPath())) {
					System.out.println("Created !");
                    processes.add(con.closeConnection());
                }
			 }
			 for (int i = 0; i < c.getNumberOfWriters(); i++) {
				 int port;
				 if (c.isRmi()) {
					 port = c.getRmiPort();
				 } else {
					 port = c.getServerPort();
				 }
				 ClientArgs args = new ClientArgs(RequestType.WRITE,
						 String.valueOf(c.getWritersIDs()[i]), c.getServerAdd(), port, c.getNumberOfAccess(), c.isRmi());
				 System.out.println("Creating writer process " + i);
				 if (con.openConnection(c.getWritersAdd()[i], c.getWritersPass()[i], c.getWritersUsername()[i], args, c.getPath())) {
					System.out.println("Created !");
					processes.add(con.closeConnection());
                }
			 }	             
        } catch (Exception e) {
            e.printStackTrace();
        }
		 return processes;
	}
		
}
