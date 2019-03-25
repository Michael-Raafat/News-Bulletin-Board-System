package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMI extends Remote {
	<T> T executeTask(Task<T> task, TaskType t) throws RemoteException;
}
