package utils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SystemConfiguration {
	private String serverAdd;
	private int serverPort;
	private int numberOfReaders;
	private int numberOfWriters;
	private int numberOfAccess;
	private String[] readersAdd, writersAdd;
	private String[] readersUsername, writersUsername;
	private String[] readersPass, writersPass;
	private int[] readersIDs, writersIDs;
	private int rmiPort;
	private boolean rmi;
	private String path;
	
	boolean error;
	
	
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
	
	public String[] getReadersUsername() {
		return readersUsername;
	}

	public String[] getWritersUsername() {
		return writersUsername;
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
	      if (properties.containsKey("path")) {
	    	  path = properties.get("path");
	      } else {
	    	  path = System.getProperty("user.dir");
	      }
	      readersUsername = new String[numberOfReaders];
	      readersIDs = new int [numberOfReaders];
		  readersAdd = new String [numberOfReaders];
		  readersPass = new String[numberOfReaders];
	      for (int i = 0; i < numberOfReaders; i++) {
	    	  if (properties.containsKey("reader" + i)) {
	    		 String kol = properties.get("reader" + i);
	    		 int indexU = kol.indexOf("@");
	    		 if (indexU == -1) {
	    			 indexU = kol.length();
	    		 }
	    		 String name = kol.substring(0, indexU);
	    		 
	    		 int indexA = kol.indexOf(" ");
	    		 if (indexA == -1) {
	    			 indexA = kol.length();
	    		 }
	    		 String add = kol.substring(indexU + 1, indexA);
	    		 readersUsername[i] = name;
	    		 readersIDs[i] = i + 1;
	    		 readersAdd[i] = add;
	    		 if (indexA != kol.length()) {
	    			 readersPass[i] = kol.substring(indexA + 1);
	    		 } else {
	    			 readersPass[i] = "";
	    		 }
	    	  } else {
	    		  error = true;
	    		  System.out.println("Missing reader with tag 'reader" + i + "'");
	    		  return;
	    	  }
	      }
	      writersUsername = new String[numberOfReaders];
	      writersIDs = new int [numberOfWriters];
	      writersAdd = new String [numberOfWriters];
	      writersPass = new String[numberOfWriters];
	      for (int i = 0; i < numberOfWriters; i++) {
	    	  if (properties.containsKey("writer" + i)) {
	    		 String kol = properties.get("writer" + i);
	    		 int indexU = kol.indexOf("@");
	    		 if (indexU == -1) {
	    			 indexU = kol.length();
	    		 }
	    		 String name = kol.substring(0, indexU);
	    		 
	    		 int indexA = kol.indexOf(" ");
	    		 if (indexA == -1) {
	    			 indexA = kol.length();
	    		 }
	    		 String add = kol.substring(indexU + 1, indexA);
				 writersUsername[i] = name;
	    		 writersIDs[i] = i + 1 + numberOfWriters;
	    		 writersAdd[i] = add;
	    		 if (indexA != kol.length()) {
	    			 writersPass[i] = kol.substring(indexA + 1);
	    		 } else {
	    			 writersPass[i] = "";
	    		 }
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
	    	 System.out.println("\t\t\tUsername : " + readersUsername[i] +"\n\t\t\tAddress : " + readersAdd[i] + "\n\t\t\tId : "
	    			 + readersIDs[i] + "\n\t\t\tPassword : " + readersPass[i]);
	      }
	      System.out.println("\tNumber of writers : " + numberOfWriters);
	      for (int i = 0; i < numberOfWriters; i++) {
	    	 System.out.println("\t\tWriter " + i + " : ");
	    	 System.out.println("\t\t\tUsername : " + writersUsername[i] + "\n\t\t\tAddress : " + writersAdd[i] + "\n\t\t\tId : "
	    			 + writersIDs[i] + "\n\t\t\tPassword : " + writersPass[i]);
	      }
	      System.out.println("\tNumber of Accesses : " + numberOfAccess);
	      System.out.println("\tPath for client executables : " + path);
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
	
	public String getPath() {
		return path;
	}
}
