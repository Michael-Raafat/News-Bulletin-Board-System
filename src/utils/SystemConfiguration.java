package utils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SystemConfiguration {
	private String userName;
	private String userPass;
	private String serverAdd;
	private int serverPort;
	private int numberOfReaders;
	private int numberOfWriters;
	private int numberOfAccess;
	private String[] readersAdd, writersAdd;
	private String[] readersPass, writersPass;
	private int[] readersIDs, writersIDs;
	private int rmiPort;
	private boolean rmi;
	
	boolean error;
	
	public String getUserName() {
		return userName;
	}

	public String getUserPass() {
		return userPass;
	}
	
	public String getServerAdd() {
		return serverAdd;
	}

	public int getServerPort() {
		return serverPort;
	}

	public int getNumberOfReaders() {
		return numberOfReaders;
	}

	public int getNumberOfWriters() {
		return numberOfWriters;
	}

	public int getNumberOfAccess() {
		return numberOfAccess;
	}

	public String[] getReadersAdd() {
		return readersAdd;
	}

	public String[] getWritersAdd() {
		return writersAdd;
	}

	public String[] getReadersPass() {
		return readersPass;
	}

	public String[] getWritersPass() {
		return writersPass;
	}

	public int[] getReadersIDs() {
		return readersIDs;
	}

	public int[] getWritersIDs() {
		return writersIDs;
	}
	
	public SystemConfiguration (String filename, boolean rmi) {
		error = false;
		this.rmi = rmi;
		List<String> lines = Collections.emptyList(); 
	    try
	    { 
	      lines = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);
	      HashMap<String, String> properties = new HashMap<String, String>();
	      for (int i = 0; i < lines.size(); i++) {
	    	  String s = lines.get(i);
	    	  properties.put(s.substring(3, s.indexOf("=")), s.substring(s.indexOf("=") + 1));
	      }
	      if (properties.containsKey("machine")) {
	    	  String kol = properties.get("machine");
	    	  int index = kol.indexOf(" ");
	    	  if (index == -1) {
	    		  index = kol.length();
	    	  }
	    	  String name = kol.substring(0, index);
	    	  userName = name;
    		  userPass = kol.substring(index + 1);
	      }
	      if (rmi) {
	    	  if (properties.containsKey("rmiregistry.port")) {
		    	  rmiPort = Integer.parseInt(properties.get("rmiregistry.port"));
		      } else {
		    	  error = true;
		    	  System.out.println("Error! missing port of rmi service!");
		    	  return;
		      }
	      }
	      if (properties.containsKey("server")) {
	    	  serverAdd =properties.get("server");
	      } else {
	    	  error = true;
	    	  System.out.println("Error! missing address of server!");
	    	  return;
	      }
	      if (properties.containsKey("server.port")) {
	    	  serverPort = Integer.parseInt(properties.get("server.port"));
	      } else {
	    	  error = true;
	    	  System.out.println("Error! missing port of server!");
	    	  return;
	      }
	      if (properties.containsKey("numberOfReaders")) {
	    	  numberOfReaders = Integer.parseInt(properties.get("numberOfReaders"));
	      } else {
	    	  error = true;
	    	  System.out.println("Error! missing number of readers!");
	    	  return;
	      }
	      if (properties.containsKey("numberOfWriters")) {
	    	  numberOfWriters = Integer.parseInt(properties.get("numberOfWriters"));
	      } else {
	    	  error = true;
	    	  System.out.println("Error! missing number of writers!");
	    	  return;
	      }
	      if (properties.containsKey("numberOfAccesses")) {
	    	  numberOfAccess = Integer.parseInt(properties.get("numberOfAccesses"));
	      } else {
	    	  error = true;
	    	  System.out.println("Error! missing number of accesses!");
	    	  return;
	      }
	      
	      readersIDs = new int [numberOfReaders];
		  readersAdd = new String [numberOfReaders];
		  readersPass = new String[numberOfReaders];
	      for (int i = 0; i < numberOfReaders; i++) {
	    	  if (properties.containsKey("reader" + i)) {
	    		  String kol = properties.get("reader" + i);
					int index = kol.indexOf(" ");
					if (index == -1) {
						index = kol.length();
					}
					String add = kol.substring(0, index);
	    		  readersIDs[i] = i + 1;
	    		  readersAdd[i] = add;
	    		  readersPass[i] = kol.substring(index);;
	    	  } else {
	    		  error = true;
	    		  System.out.println("Missing reader with tag 'reader" + i + "'");
	    		  return;
	    	  }
	      }
	      writersIDs = new int [numberOfWriters];
	      writersAdd = new String [numberOfWriters];
	      writersPass = new String[numberOfWriters];
	      for (int i = 0; i < numberOfWriters; i++) {
	    	  if (properties.containsKey("writer" + i)) {
	    		  String kol = properties.get("writer" + i);
					int index = kol.indexOf(" ");
					if (index == -1) {
						index = kol.length();
					}
					String add = kol.substring(0, index);
				  writersIDs[i] = i + 1 + numberOfReaders;
	    		  writersAdd[i] = add;
	    		  writersPass[i] = kol.substring(index);;
	    	  } else {
	    		  error = true;
	    		  System.out.println("Missing reader with tag 'reader" + i + "'");
	    		  return;
	    	  }
	      }
	      System.out.println("System configuration detected : ");
	      System.out.println("\tServer Address : " + serverAdd);
	      System.out.println("\tServer Port : " + serverPort);
	      if (rmi) {
	    	  System.out.println("\tRMI Port : " + rmiPort);
	      }
	      System.out.println("\tNumber of readers : " + numberOfReaders);
	      for (int i = 0; i < numberOfReaders; i++) {
	    	 System.out.println("\t\tReader " + i + " : ");
	    	 System.out.println("\t\t\tAddress : " + readersAdd[i] + "\n\t\t\tId : "
	    			 + readersIDs[i] + "\n\t\t\tPassword : " + readersPass[i]);
	      }
	      System.out.println("\tNumber of writers : " + numberOfWriters);
	      for (int i = 0; i < numberOfWriters; i++) {
	    	 System.out.println("\t\tWriter " + i + " : ");
	    	 System.out.println("\t\t\tAddress : " + writersAdd[i] + "\n\t\t\tId : "
	    			 + writersIDs[i] + "\n\t\t\tPassword : " + writersPass[i]);
	      }
	      System.out.println("\tNumber of Accesses : " + numberOfAccess);
	    } catch (Exception e) { 
	    	e.printStackTrace();
	      error = true;
	    }
	}
	
	public int getRmiPort() {
		return rmiPort;
	}

	public boolean validConfiguration() {
		return !error;
	}

	public boolean isRmi() {
		return rmi;
	}
}
