package server.rmi;

import java.rmi.RemoteException;
import java.util.concurrent.atomic.AtomicInteger;

import server.SharedServerObject;
import utils.log.record.ReadRecord;
import utils.log.record.Record;
import utils.log.record.WriteRecord;
import utils.types.RequestType;

public class ClientController implements RemoteAccessController {

	private AtomicInteger rSeq;
	private SharedServerObject object;
	private Thread t;
	private int requests;
	
	public ClientController(int req, Thread parent) {
		super();
		rSeq = new AtomicInteger(1);
		requests = req;
		object = SharedServerObject.getSharedObject();
		t = parent;
	}
	public String executeTask(Integer id, String requestType, Integer value) throws RemoteException {
		int rSeq = this.rSeq.getAndIncrement();
		RequestType type = RequestType.toRequestType(requestType);
		Record rec = null;
		if (type == RequestType.READ) {
			ReadRecord recr = object.readValue(id, rSeq);
			rec = recr;
		} else if (type == RequestType.WRITE) {
			WriteRecord recw = object.writeValue(id, rSeq, value);
			rec = recw;
		}
		if (rSeq == requests) {
			synchronized (t) {
				t.notifyAll();
			}
		}
		System.out.println("Done request #" + rSeq);
		if (rec != null) {
			return rec.toString();
		} else {
			return null;
		}
	}
	
	public AtomicInteger getRseq() {
		return rSeq;
	}
}
