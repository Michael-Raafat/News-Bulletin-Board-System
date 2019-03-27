package server.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Semaphore;

import server.SharedServerObject;

public class RMIServerWorker extends Thread {
	private int port;
	private int rmiPort;
    private int requestNumber;
    private String serverAddress;
    private static Registry reg;
    private static ClientController controller;
    private static RemoteAccessController rController;

    public RMIServerWorker(int port, int rmiPort, int requestNumber,
    		String address) {
        this.port = port;
        this.requestNumber = requestNumber;
        this.rmiPort = rmiPort;
        this.serverAddress = address;
    }

    public void run() {
    	Semaphore sem = new Semaphore(-requestNumber + 1); 
    	SharedServerObject object = SharedServerObject.getSharedObject();
    	controller = new ClientController(sem);
    	try {
    		System.setProperty("java.rmi.server.hostname",serverAddress);
    		LocateRegistry.createRegistry(rmiPort);
			rController = (RemoteAccessController) UnicastRemoteObject.exportObject(controller, port);
			reg = LocateRegistry.getRegistry(rmiPort);
			reg.rebind("RemoteAccessController", rController);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	System.out.println("Server registered RMI successfully!");
    	try {
			sem.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("Server served all requests!");
        object.getLog().writeServerLog();
    }
}
