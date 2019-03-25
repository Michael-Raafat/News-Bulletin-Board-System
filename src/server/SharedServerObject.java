package server;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import utils.log.Log;
import utils.log.record.ReadRecord;
import utils.log.record.WriteRecord;

public class SharedServerObject {
	private int val;
	private AtomicInteger sSeq;
	private AtomicInteger nRead;
	private ReadWriteLock valLock;
	private Log log;
	
	private static SharedServerObject object = null;
	
	private SharedServerObject() {
		val = -1;
		sSeq = new AtomicInteger(1);
		nRead = new AtomicInteger(0);
		valLock = new ReentrantReadWriteLock(true);
		log = new Log();
	}
	
	public static SharedServerObject getSharedObject() {
		if (object == null) {
			object = new SharedServerObject();
		}
		return object;
	}
	
	public ReadRecord readValue(int id, int rSeq) {
		nRead.incrementAndGet();
		valLock.readLock().lock();
		int value = val;
		int sSequ = sSeq.getAndIncrement();
		int rNum = nRead.get();
		ReadRecord rec = new ReadRecord(sSequ, rSeq, value, rNum, id);
		valLock.readLock().unlock();
		log.addToLog(rec);
		nRead.decrementAndGet();
		return rec;
	}
	
	public WriteRecord writeValue(int id, int rSeq, int value) {
		valLock.writeLock().lock();
		val = value;
		int sSequ = sSeq.getAndIncrement();
		WriteRecord rec = new WriteRecord(sSequ, rSeq, value, id);
		valLock.writeLock().unlock();
		log.addToLog(rec);
		return rec;
	}
	
	public Log getLog() {
		return log;
	}
	
}
