package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import utils.log.record.Record;

public class ClientWorker extends Thread {
	private Socket s;
	private int rSeq;
	private SharedServerObject object;
	
	public ClientWorker(Socket s, int rSeq, SharedServerObject object) {
		this.s = s;
		this.rSeq = rSeq;
		this.object = object;
	}
	
	public void run() {
		String request = null;
        BufferedReader input = null;
        PrintWriter output = null;
		try {
            input = new BufferedReader(new InputStreamReader(s.getInputStream()));
            output = new PrintWriter(s.getOutputStream());
        } catch (IOException e) {
            System.out.println("IO error in server thread");
        }
        try {
            request = input.readLine();
            String[] splittedData = request.split(" ");
            String type = splittedData[0];
            int id = Integer.parseInt(splittedData[1]);
            int val = -1;
            Record r = null;
            // A reader has entered the system, increase the rNum 
            if (type.equals("W")) {
                val = Integer.parseInt(splittedData[2]);
                r = object.writeValue(id, rSeq, val);
            } else if (type.equals("R")) {
            	r = object.readValue(id, rSeq);
            } else {
            	throw new RuntimeException("Invalid operation !");
            }

            String response = r.toString();

            /* Respond to the client */
            output.println(response);
            output.flush();

        } catch (Exception e) {
            System.out.println("IO Error/ Client " + request + " terminated");
        } finally {
            try {
                System.out.println("Connection Closing..");
                if (input != null) {
                    input.close();
                    System.out.println("Socket Input Stream Closed");
                }
                if (output != null) {
                    output.close();
                    System.out.println("Socket Out Closed");
                }
                if (s != null) {
                    s.close();
                    System.out.println("Socket Closed");
                }
            } catch (IOException ie) {
                System.out.println("Socket Close Error");
            }
        }
	}
}
