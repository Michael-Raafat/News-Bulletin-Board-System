package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
public class Client {
	public static void main(String args[]) throws IOException {
        String address = args[0];
        int port = Integer.parseInt(args[1]);
        String type = args[2];
        String id = args[3];
        int noOfAccesses = Integer.parseInt(args[4]);
        int readers = 0; 
        if(type == "READER")
        	readers++;
        Socket s = null;
        // to send to server
        String request = null;
        
        BufferedReader input = null;
        // to write in file
        PrintWriter output = null;
        PrintWriter fwriter = new PrintWriter(   "logs/" + "log" + type == "READER" ? String.valueOf(id) : String.valueOf(id+ readers) + ".txt", "UTF-8");
        fwriter.println("Client type: " + type);
        String header = (type.equals("READER")? "rSeq---sSeq---oVal" : "rSeq---sSeq");
       fwriter.println(header);
        for (int i = 0; i < noOfAccesses; i++) {
            try {
                s = new Socket(address, port);
                input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                output = new PrintWriter(s.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
                System.err.print("IO Exception");
            }

            String response = "";

            try {
            	
                request = type + " " + id;
                output.println(request);
                output.flush();
                response = input.readLine();
                // assuming response is 1 2 3
                System.out.println("rSeq: " + response.split(" ")[0]);
                System.out.println("sSeq: " + response.split(" ")[1]);
                if(type.equals("READER")) 
                    System.out.println("oVal: " + response.split(" ")[2]);
                
                System.out.println("__________________");
                fwriter.println(response.split(" ")[0]+"---"+response.split(" ")[1]+
                    		(type.equals("READER")? "---"+response.split(" ")[2]:""));
                

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Socket read Error");
            } finally {
                input.close();
                output.close();
                s.close();
                System.out.println("Connection Closed");
            }
            System.out.println("--------------------------------------");
        }
        fwriter.close();
    }        
}
