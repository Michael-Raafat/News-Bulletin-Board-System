package server.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteAccessController extends Remote {
	String executeTask(Integer id, String requestType, Integer value) throws RemoteException;
}
