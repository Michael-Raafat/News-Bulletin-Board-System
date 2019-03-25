package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SSHConnection {
	private Process p;
	private BufferedReader in;
	private static final String SOCK_CLIENT = "clientV1.jar";
	private static final String RMI_CLIENT = "clientV2.jar";
	//creating new ssh connection 
	public boolean openConnection(String hostAdd, String password, ClientArgs args) {
		boolean success = false;
		try {
			String path = System.getProperty("user.dir");
			String clientJar;
			if (args.rmi) {
				clientJar = RMI_CLIENT;
			} else {
				clientJar = SOCK_CLIENT;
			}
			p = Runtime.getRuntime().exec("ssh " + hostAdd + " cd " + path + " ; java -jar " + clientJar + " " + args.toString() +" ; exit");
			in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			System.out.println("data from process : " + recvData());
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return success;
	}
	
	//recieving data from ssh server
	public String recvData () {
		String input = "";
		try {
			String s = in.readLine();
			input += s;
			while (in.ready()) {
			  s = in.readLine();
			  input += s;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return input;
	}
	
	public Process closeConnection () {
		Process k = null;
		try {
			k = p;
			in.close();
			p = null;
			in = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return k;
	}
	
}
