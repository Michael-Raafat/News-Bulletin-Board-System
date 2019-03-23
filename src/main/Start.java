package main;

import java.util.List;

import utils.FReader;
import utils.SSHConnection;

public class Start {
	private static final int securePort = 22;
	
	private static String serverAdd;
	private static int serverPort;
	private static int numberOfReaders;
	private static int numberOfWriters;
	private static int numberOfAccess;
	private static int[] readersIDs, writersIDs;
	private static String[] readersAdd, writersAdd;
	private static FReader fr = new FReader();
	
	public static void main(String[] args) {
		List l = fr.readFileInList("src//system.properties.txt");
		fillArgs(l);
		createServer();
		createClient();
	}
	private static void fillArgs(List<String> l) {
		
		for (int i = 0; i < l.size(); i++) {
			String s = l.get(i);
			if (i == 0)
				serverAdd = s.substring(s.indexOf("=")+1);
			else if(i == 1)
				serverPort = Integer.parseInt(s.substring(s.indexOf("=")+1));
			else if (i == 2) {
				numberOfReaders =  Integer.parseInt(s.substring(s.indexOf("=")+1));
				readersIDs = new int [numberOfReaders];
				readersAdd = new String [numberOfReaders];
				//System.out.println(serverAdd +" "+serverPort+" "+ " "+ numberOfReaders);
			}
			
			else if (i > 2 && i < 3 + numberOfReaders) {
                readersAdd[i - 3] = s.split("=")[1];
                String number = s.split("=")[0];
                readersIDs[i - 3] = s.split("=")[0].charAt(number.length() - 1) - '0' + 1;
                //System.out.println(readersAdd[i - 3]+" "+readersIDs[i - 3]);
			}
			else if (i == 3 + numberOfReaders) {
				numberOfWriters =  Integer.parseInt(s.substring(s.indexOf("=")+1));
				writersIDs = new int [numberOfWriters];
				writersAdd = new String [numberOfWriters];
				//System.out.println(numberOfWriters);
			}
				
			else if (i > numberOfReaders + 3 && i < numberOfReaders + numberOfWriters + 4) {
				writersAdd[i - 4 - numberOfReaders] = s.split("=")[1];
                String number = s.split("=")[0];
                writersIDs[i - 4 - numberOfReaders] = s.split("=")[0].charAt(number.length() - 1) - '0' + 1;
                //System.out.println(writersAdd[i - 4 - numberOfReaders]+" "+writersIDs[i - 4 - numberOfReaders]);
			}
			else {
				numberOfAccess = Integer.parseInt(s.substring(s.indexOf("=")+1));
				//System.out.println(numberOfAccess);
			}
		}
	}
	@SuppressWarnings(value = { "Untested" })
	public static void createServer(){
		// calling server.java to create server and listen to server socket
		SSHConnection con = new SSHConnection();
		if (con.openConnection(serverAdd, securePort, "serverName", "pass", 120000)) {
            try {
                con.sendCommand("cd .//server; \n");
                Thread.sleep(300);
                //compile
                con.sendCommand("javac Server.java \n");
                Thread.sleep(300);

                int Requests = (readersIDs.length + writersIDs.length) * numberOfAccess;
                //run and send args
                //Args to be discussed
                con.sendCommand("java Server " + serverPort + " " + Requests + " \n");
                Thread.sleep(300);

                con.closeConnection();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Error");
        }
	}
	@SuppressWarnings(value = { "Untested" })
	public static void createClient(){
		// calling client.java to create readers and writers
		SSHConnection con = new SSHConnection();
		 try {
			 con.sendCommand("cd .//client; \n");
			 Thread.sleep(300);
	            for (int i = 0; i < readersIDs.length; i++) {
	            													//replace userName and pass
	                if (con.openConnection(readersAdd[i], securePort, "ClientName", "pass", 120000)) {
	                    

	                    con.sendCommand("javac Client.java \n");
	                    Thread.sleep(300);

	                    con.sendCommand("java Client " + serverAdd + " " + serverPort + " READER " + readersIDs[i] + " "
	                            + numberOfAccess + " " + readersIDs.length+ " \n");
	                    Thread.sleep(300);

	                }
	            }

	            for (int i = 0; i < writersIDs.length; i++) {
	            													//replace userName and pass
	                if (con.openConnection(writersAdd[i], securePort, "ClientName", "pass", 120000)) {
	                    Thread.sleep(300);

	                    con.sendCommand("javac Client.java \n");
	                    Thread.sleep(300);
	                    //Args to be discussed
	                    con.sendCommand("java Client " + serverAdd + " " + serverPort + " WRITER " + writersIDs[i] + " "
	                            + numberOfAccess + " " + writersIDs.length+ " \n");
	                    Thread.sleep(300);

	                }
	            }
	             con.closeConnection();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}
		
}
