package remote;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
public class Engine implements RMI{

	public Engine() {
		super();
	}
	
	public static void main(String[] args) {
		if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "RMI";
            RMI engine = new Engine();
            RMI stub =(RMI) UnicastRemoteObject.exportObject(engine, 1099);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("Engine bound");
        } catch (Exception e) {
            System.err.println("Engine exception:");
            e.printStackTrace();
        }
	}

	public <T> T executeTask(Task<T> task, TaskType t) throws RemoteException {
		return task.execute(t);
	}

}
