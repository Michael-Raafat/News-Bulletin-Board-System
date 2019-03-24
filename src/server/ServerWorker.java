package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerWorker extends Thread {
	
    private int port;
    private int requestNumber;
    private int rSeq;

    public ServerWorker(int port, int requestNumber) {
        this.port = port;
        this.requestNumber = requestNumber;
        this.rSeq = 1;
    }

    public void run() {
    	SharedServerObject object = SharedServerObject.getSharedObject();
        Socket Acceptsocket = null;
        ServerSocket serverSocket = null;
        System.out.println("Turning Server On");
        try {
        	serverSocket = new ServerSocket(port); 
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server error");
        }
        System.out.println("Server On...");
        List<Thread> threads = new ArrayList<Thread>();
        while (requestNumber > 0) {
            try {
            	requestNumber--;
                Acceptsocket = serverSocket.accept();
                System.out.println("Accepted connection #" + rSeq);
                ClientWorker thread = new ClientWorker(Acceptsocket, rSeq, object);
                rSeq++;
                thread.start();
                threads.add(thread);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Connection Error");
            }
        }
        for (int i = 0; i < threads.size(); i++) {
			try {
				threads.get(i).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
        object.getLog().writeServerLog();
    }
}
