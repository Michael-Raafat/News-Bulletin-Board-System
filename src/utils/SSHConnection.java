package utils;
import java.util.Properties;
import java.io.IOException;
import java.io.OutputStream;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SSHConnection {
	//creating jsch variable
	private JSch mJschSSH = null;
	//creating ssh session
	private Session mSSHSession = null;
	//creating new ssh channel
	private Channel mSSHChannel = null;
	private OutputStream outputStream = null;
	
	//creating new ssh connection 
	public boolean openConnection(String hostAdd, int port, String userName,
			String password, int timeout) {
		boolean success = false;
		mJschSSH = new JSch();
		/* check no key to login */
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        mJschSSH.setConfig(config);
        
        
        try{
        	//get session , set it's password and connect to ssh server
        	mSSHSession = mJschSSH.getSession(userName, hostAdd, port);
        	mSSHSession.setPassword(password);
        	mSSHSession.connect(timeout);
        	// connect channel to ssh server
        	mSSHChannel = this.mSSHSession.openChannel("shell");
        	mSSHChannel.connect();

        	mSSHChannel.setInputStream(System.in);
            this.outputStream = mSSHChannel.getOutputStream();
            success = true;
			System.out.println(">>>");

        } catch (Exception e) {
        	e.printStackTrace();
        }
        
		return success;
	}
	// sending commands to ssh server
	public boolean sendCommand (String command) {
		boolean success = false;
		try {
			if (mSSHChannel != null) {
				mSSHChannel.getOutputStream().write(command.getBytes());
				mSSHChannel.getOutputStream().flush();
				success = true;
			}
		}catch (Exception e) {
	        	e.printStackTrace();
	    }
		return success;
	}
	
	//recieving data from ssh server
	public String recvData () {
		String input = null;
		try {
			if(mSSHChannel != null && mSSHChannel.getInputStream().available() > 0) {
				int available = mSSHChannel.getInputStream().available();
				while (available > 0) {
					byte[] buffer = new byte[available];
					int read = mSSHChannel.getInputStream().read(buffer);
					available = available - read;
					input += String.valueOf(buffer);
				}
			} 
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		System.out.println(input);

		return input;
	}
	
	public void closeConnection () {
		if (mSSHSession != null)
			mSSHSession.disconnect();
		if (mSSHChannel != null)
			mSSHChannel.disconnect();
		mJschSSH = null;		
	}
	
}
