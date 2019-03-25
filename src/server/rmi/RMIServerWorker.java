package server.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import server.SharedServerObject;

public class RMIServerWorker extends Thread {
	private int port;
	private int rmiPort;
    private int requestNumber;

    public RMIServerWorker(int port, int rmiPort, int requestNumber) {
        this.port = port;
        this.requestNumber = requestNumber;
        this.rmiPort = rmiPort;
    }

    public void run() {
    	SharedServerObject object = SharedServerObject.getSharedObject();
    	ClientController controller = new ClientController(requestNumber, this);
    	try {
    		LocateRegistry.createRegistry(rmiPort);
			RemoteAccessController rController = (RemoteAccessController) UnicastRemoteObject.exportObject(controller, port);
			Registry reg = LocateRegistry.getRegistry(rmiPort);
			reg.rebind("RemoteAccessController", rController);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	System.out.println("Server registered RMI successfully!");
    	try {
    		synchronized (this) {
    			this.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        System.out.println("Server served all requests!");
        object.getLog().writeServerLog();
    }
}
