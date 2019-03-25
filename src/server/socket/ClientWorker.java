package server.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import server.SharedServerObject;
import utils.log.record.Record;
import utils.types.RequestType;

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
            int id = Integer.parseInt(splittedData[1]);
            int val = -1;
            Record r = null;
            RequestType type = RequestType.toRequestType(splittedData[0]);
            // A reader has entered the system, increase the rNum 
            if (type == RequestType.WRITE) {
                val = Integer.parseInt(splittedData[2]);
                r = object.writeValue(id, rSeq, val);
            } else if (type == RequestType.READ) {
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
                if (input != null) {
                    input.close();
                }
                if (output != null) {
                    output.close();
                }
                if (s != null) {
                    s.close();
                }
            } catch (IOException ie) {
                System.out.println("Socket Close Error");
            }
        }
	}
}
