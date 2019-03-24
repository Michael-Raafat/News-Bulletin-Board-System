package utils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class SystemConfiguration {
	private String serverAdd;
	private int serverPort;
	private int numberOfReaders;
	private int numberOfWriters;
	private int numberOfAccess;
	private String[] readersAdd, writersAdd;
	private String[] readersPass, writersPass;
	private int[] readersIDs, writersIDs;
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
	
	public SystemConfiguration (String filename) {
		error = false;
		List<String> lines = Collections.emptyList(); 
	    try
	    { 
	      lines = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);
	      for (int i = 0; i < lines.size(); i++) {
				String s = lines.get(i);
				if (i == 0)
					serverAdd = s.substring(s.indexOf("=")+1);
				else if(i == 1)
					serverPort = Integer.parseInt(s.substring(s.indexOf("=")+1));
				else if (i == 2) {
					numberOfReaders =  Integer.parseInt(s.substring(s.indexOf("=")+1));
					readersIDs = new int [numberOfReaders];
					readersAdd = new String [numberOfReaders];
					readersPass = new String[numberOfReaders];
				} else if (i > 2 && i < 3 + numberOfReaders) {
					String readerId = s.substring(9, s.indexOf("="));
					String kol = s.substring(s.indexOf("=") + 1);
					int index = kol.indexOf(" ");
					if (index == -1) {
						index = kol.length();
					}
					String add = kol.substring(0, index);
	                readersAdd[i - 3] = add;
	                readersIDs[i - 3] = Integer.parseInt(readerId) + 1;
	                readersPass[i - 3] = kol.substring(index);
				} else if (i == 3 + numberOfReaders) {
					numberOfWriters =  Integer.parseInt(s.substring(s.indexOf("=")+1));
					writersIDs = new int [numberOfWriters];
					writersAdd = new String [numberOfWriters];
					writersPass = new String[numberOfWriters];
				} else if (i > numberOfReaders + 3 && i < numberOfReaders + numberOfWriters + 4) {
					String writerId = s.substring(9, s.indexOf("="));
					String kol = s.substring(s.indexOf("=") + 1);
					int index = kol.indexOf(" ");
					if (index == -1) {
						index = kol.length();
					}
					String add = kol.substring(0, index);
	                writersAdd[i - 4 - numberOfReaders] = add;
	                writersIDs[i - 4 - numberOfReaders] = Integer.parseInt(writerId) + 1 + numberOfReaders;
	                writersPass[i - 4 - numberOfReaders] = kol.substring(index);
	            } else {
					numberOfAccess = Integer.parseInt(s.substring(s.indexOf("=")+1));
				}
			}
	      System.out.println("System configuration detected : ");
	      System.out.println("\tServer Address : " + serverAdd);
	      System.out.println("\tServer Port : " + serverPort);
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
	    } catch (Exception e) { 
	    	e.printStackTrace();
	      error = true;
	    }
	}
	
	public boolean validConfiguration() {
		return !error;
	}
}
