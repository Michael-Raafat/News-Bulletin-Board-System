package client.worker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

import utils.log.Log;
import utils.log.record.ReadRecord;
import utils.log.record.WriteRecord;
import utils.types.RequestType;

public abstract class Worker {
	protected Log log;
	private Random rand;
	private RequestType type;
	private final static int MAX_SLEEP_TIME = 10000;
	
	public Worker(RequestType type) {
		log = new Log();
		rand = new Random();
		this.type = type;
	}
	
	protected abstract String generate_request_message(String id);
	
	public void execute(String id, String serverAddress, int port, int num_requests) {
		Socket s = null;
		BufferedReader input = null;
		PrintWriter output = null;
        // to send to server
        String request = this.generate_request_message(id);
        for (int i = 0; i < num_requests; i++) {
        	try {
				Thread.sleep(rand.nextInt(MAX_SLEEP_TIME));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            try {
                s = new Socket(serverAddress, port);
                input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                output = new PrintWriter(s.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
                System.err.print("IO Exception");
            }

            String response = "";

            try {
                output.println(request);
                output.flush();
                response = input.readLine();
                if (type == RequestType.READ) {
                	ReadRecord rec = new ReadRecord(response);
                	log.addToLog(rec);
                } else if (type == RequestType.WRITE) {
                	WriteRecord rec = new WriteRecord(response);
                	log.addToLog(rec);
                }
                

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Socket read Error");
            } finally {
                try {
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
                System.out.println("Connection Closed");
            }
        }
	}
	
	public abstract void print_log(String id);
}
