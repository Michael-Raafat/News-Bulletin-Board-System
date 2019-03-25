package utils.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import utils.log.record.ReadRecord;
import utils.log.record.WriteRecord;

public class Log {
	private List<ReadRecord> readRecords;
	private List<WriteRecord> writeRecords;
	private static final String LOG_DIR = "logs";
	public Log() {
		readRecords = Collections.synchronizedList(new ArrayList<ReadRecord>());
		writeRecords = Collections.synchronizedList(new ArrayList<WriteRecord>());
	}
	
	public void addToLog(ReadRecord rec) {
		readRecords.add(rec);
	}
	
	public void addToLog(WriteRecord rec) {
		writeRecords.add(rec);
	}
	
	private void makeDir() {
		File directory = new File(LOG_DIR);
	    if (! directory.exists()){
	        directory.mkdir();
	    }
	}
	public void writeServerLog() {
		makeDir();
		PrintWriter writer = null;
        try {
            writer = new PrintWriter(LOG_DIR + File.separator + "server_log.txt", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // Print readers records 
        writer.println("Readers:");
        writer.println("--------");

        writer.format("%20s %20s %20s %20s\n", "sSeq", "oVal", "rID", "rNum");

        for (int i = 0; i < readRecords.size(); i++) {
        	ReadRecord rec = readRecords.get(i);
        	writer.format("%20s %20s %20s %20s\n",
        			String.valueOf(rec.sSeq),
        			String.valueOf(rec.oVal),
					String.valueOf(rec.rId),
					String.valueOf(rec.rNum));
        }

        writer.println();

        // Print writers records 
        writer.println("Writers:");
        writer.println("--------");
        
        writer.format("%20s %20s %20s\n", "sSeq", "oVal", "wID");

        for (int i = 0; i < writeRecords.size(); i++) {
        	WriteRecord rec = writeRecords.get(i);
        	writer.format("%20s %20s %20s\n",
        			String.valueOf(rec.sSeq),
        			String.valueOf(rec.oVal),
					String.valueOf(rec.wId));
        }
        
        // Close writer 
        writer.close();
	}
	
	public void writeReaderLog(String id) {
		makeDir();
		PrintWriter writer = null;
        try {
            writer = new PrintWriter(LOG_DIR + File.separator + "log" + id + ".txt", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        writer.println("Client type : Reader");
        writer.println("Client Name : " + id);
        writer.format("%20s %20s %20s\n", "rSeq", "sSeq",  "oVal");

        for (int i = 0; i < readRecords.size(); i++) {
        	ReadRecord rec = readRecords.get(i);
        	writer.format("%20s %20s %20s\n",
        			String.valueOf(rec.rSeq),
        			String.valueOf(rec.sSeq),
        			String.valueOf(rec.oVal));
        }
        writer.close();
	}
	
	public void writeWriterLog(String id) {
		makeDir();
		PrintWriter writer = null;
        try {
            writer = new PrintWriter(LOG_DIR + File.separator + "log" + id + ".txt", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        writer.println("Client type : Writer");
        writer.println("Client Name : " + id);
        writer.format("%20s %20s\n", "rSeq", "sSeq");

        for (int i = 0; i < writeRecords.size(); i++) {
        	WriteRecord rec = writeRecords.get(i);
        	writer.format("%20s %20s\n",
        			String.valueOf(rec.rSeq),
        			String.valueOf(rec.sSeq));
        }
        writer.close();
	}
}
