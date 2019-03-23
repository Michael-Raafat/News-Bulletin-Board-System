package server;

	import java.io.*;
	import java.net.ServerSocket;
	import java.net.Socket;
	import java.util.ArrayList;
	import java.util.List;
	import java.util.Random;

	public class Server {

	    protected static List<List<Integer>> readersLog;
	    protected static List<List<Integer>> writersLog;

	    protected static int R_NUM = 0;
	    protected static int S_SEQ = 0;
	    protected static int O_VAL = -1;
	    private static int R_SEQ = 0;
	    

	    public static void main(String args[]) {
	        int port = Integer.parseInt(args[0]);
	        int numberOfAccesses = Integer.parseInt(args[1]);

	        readersLog = new ArrayList<List<Integer>>();
	        writersLog = new ArrayList<List<Integer>>();

	        Socket Acceptsocket = null;
	        ServerSocket serverSocket = null;
	        System.out.println("Server On");
	        try {
	        	serverSocket = new ServerSocket(port); 
	        } catch (IOException e) {
	            e.printStackTrace();
	            System.out.println("Server error");
	        }

	        while (numberOfAccesses > 0) {
	            try {
	                numberOfAccesses--;
	                Acceptsocket = serverSocket.accept();
	                R_SEQ++;
	                ServerThread thread = new ServerThread(Acceptsocket, R_SEQ);
	                thread.start();
	            } catch (Exception e) {
	                e.printStackTrace();
	                System.out.println("Connection Error");
	            }
	        }

	        // Make sure the main thread closes the last thread 
	        while (Thread.activeCount() > 1) {
	            try {
	                Thread.sleep(100);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }

	        printServerLog();
	    }

	    private static void printServerLog() {

	        PrintWriter writer = null;
	        try {
	            writer = new PrintWriter("logs" + "/" + "server_log.txt", "UTF-8");
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        }

	        // Print readers records 
	        writer.println("Readers:");
	        writer.println("--------");

	        
	        writer.println("sSeq---oVal---rID---rNum");

	        StringBuilder recordBody;
	        for (int i = 0; i < readersLog.size(); i++) {
	            recordBody = new StringBuilder();
	            for (int token : readersLog.get(i)) {
	                recordBody.append(token);
	                recordBody.append("\t\t");
	            }

	            recordBody.deleteCharAt(recordBody.length() - 1);
	            writer.println(new String(recordBody));
	        }

	        writer.println();

	        // Print writers records 
	        writer.println("Writers:");
	        writer.println("--------");

	        
	        writer.println("sSeq---oVal---wID");
	        // not sure of format of files :(
	        for (int i = 0; i < writersLog.size(); i++) {
	            recordBody = new StringBuilder();
	            for (int token : writersLog.get(i)) {
	                recordBody.append(token);
	                recordBody.append("\t\t");
	            }
	            recordBody.deleteCharAt(recordBody.length() - 1);
	            writer.println(new String(recordBody));
	        }
	        // Close writer 
	        writer.close();
	    }
	}

	class ServerThread extends Thread {

	    String request = null;
	    BufferedReader input = null;
	    PrintWriter output = null;
	    Socket s = null;

	    int rSeq;
	    Random rand;

	    public ServerThread(Socket s, int rSeq) {
	        this.s = s;
	        this.rSeq = rSeq;
	        rand = new Random();
	    }

	    public void run() {
	        try {
	            input = new BufferedReader(new InputStreamReader(s.getInputStream()));
	            output = new PrintWriter(s.getOutputStream());
	        } catch (IOException e) {
	            System.out.println("IO error in server thread");
	        }
	        try {
	            request = input.readLine();
	            String type = request.split(" ")[0];
	            int id = Integer.parseInt(request.split(" ")[1]);

	            // A reader has entered the system, increase the rNum 
	            if (type.equals("READER")) {
	                Server.R_NUM++;
	            }

	            // Sleep to give the illusion of a real system 
	            int randomTime = rand.nextInt(10000);
	            System.out.println(randomTime);
	            Thread.sleep(randomTime);

	            // Operate if writer 
	            if (type.equals("WRITER")) {
	                Server.O_VAL = id;
	            }

	            // When thread is about to finish 
	            Server.S_SEQ++;
	            String response = (type.equals("WRITER") ? 
	            		this.rSeq+" "+Server.S_SEQ : this.rSeq+" "+Server.S_SEQ + " " + String.valueOf(Server.O_VAL));
		        
	            // Write this thread record in its list 
	            addToLogs(type, Server.S_SEQ, Server.O_VAL, id, Server.R_NUM);

	            /* Respond to the client */
	            output.println(response);
	            output.flush();

	            // When a reader client is about to finish, decrement the rNum variable 
	            if (type.equals("READER")) {
	                Server.R_NUM--;
	            }
	        } catch (Exception e) {
	            request = this.getName(); //reused String line for getting thread name
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

	    private void addToLogs(String type, int sSeq, int oVal, int id, int rNum) {
	        ArrayList<Integer> record = new ArrayList<Integer>();
	        record.add(sSeq);
	        record.add(oVal);
	        record.add(id);

	        if (type.equals("READER")) {
	            record.add(rNum);
	            Server.readersLog.add(record);
	        } else {
	            Server.writersLog.add(record);
	        }
	    }
	}
