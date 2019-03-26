package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHConnection {
	private Process p;
	private BufferedReader in;
	private static final String SOCK_CLIENT = "clientV1.jar";
	private static final String RMI_CLIENT = "clientV2.jar";
	private Session session;
	private Channel channel;
	
	
	//creating new ssh connection 
	public boolean openConnection(String hostAdd, String password, String username, ClientArgs args) {
		boolean success = false;
		try {
			String path = System.getProperty("user.dir");
			java.util.Properties config = new java.util.Properties(); 
	    	config.put("StrictHostKeyChecking", "no");
			JSch jsch = new JSch();
			jsch.setConfig(config);
			session=jsch.getSession(username, hostAdd, 22);
	    	session.setPassword(password);
	    	session.setConfig(config);
	    	session.connect(10000);
			System.out.println("Connected");
	    	
			
			String clientJar;
			if (args.rmi) {
				clientJar = RMI_CLIENT;
			} else {
				clientJar = SOCK_CLIENT;
			}
			channel=session.openChannel("exec");
	        ((ChannelExec)channel).setCommand(" cd " + path + " ; java -jar " +
	        			clientJar + " " + args.toString() +" ; exit");
	        channel.setInputStream(null);
	        ((ChannelExec)channel).setErrStream(System.err);
	        
	        in = new BufferedReader(new InputStreamReader(channel.getInputStream()));
	        channel.connect();
	        
			//p = Runtime.getRuntime().exec("ssh " + hostAdd + " cd " + path + " ; java -jar " + clientJar + " " + args.toString() +" ; exit");
			
			System.out.println("data from process : " + recvData());
			success = true;
			
	        System.out.println("DONE");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSchException e) {
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
			channel.disconnect();
	        session.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return k;
	}
	
}
