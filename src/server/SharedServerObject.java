package server;

import java.util.Random;
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
	
	private static final int MAX_SLEEP_TIME = 10000;
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
		valLock.readLock().lock();
		int rNum = nRead.incrementAndGet();
		Random rand = new Random();
		try {
			Thread.sleep(rand.nextInt(MAX_SLEEP_TIME));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int value = val;
		int sSequ = sSeq.getAndIncrement();
		ReadRecord rec = new ReadRecord(sSequ, rSeq, value, rNum, id);
		nRead.decrementAndGet();
		valLock.readLock().unlock();
		log.addToLog(rec);
		return rec;
	}
	
	public WriteRecord writeValue(int id, int rSeq, int value) {
		valLock.writeLock().lock();
		Random rand = new Random();
		try {
			Thread.sleep(rand.nextInt(MAX_SLEEP_TIME));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
