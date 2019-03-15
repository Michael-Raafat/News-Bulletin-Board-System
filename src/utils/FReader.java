package utils;

import java.nio.charset.StandardCharsets; 
import java.nio.file.*;
import java.util.Collections;
import java.util.List;
import java.io.*; 

public class FReader {
	
	public static List<String> readFileInList(String fileName) 
	  { 
	  
	    List<String> lines = Collections.EMPTY_LIST; 
	    try
	    { 
	      lines = 
	       Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8); 
	       //System.out.println(lines.get(0));
	    } 
	  
	    catch (IOException e) 
	    { 
	  
	      // do something 
	      e.printStackTrace(); 
	    } 
	    return lines; 
	  }
}
