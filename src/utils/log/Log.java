package utils.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;

import utils.log.record.ReadRecord;
import utils.log.record.WriteRecord;

public class Log {
	private List<ReadRecord> readRecords;
	private List<WriteRecord> writeRecords;
	
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
	
	public void writeServerLog() {
		PrintWriter writer = null;
        try {
            writer = new PrintWriter("logs" + File.separator + "server_log.txt", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // Print readers records 
        writer.println("Readers:");
        writer.println("--------");

        Formatter formatter = new Formatter();
        writer.println(formatter.format("%20s %20s %20s %20s", "sSeq", "oVal", "rID", "rNum"));

        for (int i = 0; i < readRecords.size(); i++) {
        	ReadRecord rec = readRecords.get(i);
        	writer.println(formatter.format("%20s %20s %20s %20s",
        			String.valueOf(rec.sSeq),
        			String.valueOf(rec.oVal),
					String.valueOf(rec.rId),
					String.valueOf(rec.rNum)));
        }

        writer.println();

        // Print writers records 
        writer.println("Writers:");
        writer.println("--------");
        
        writer.println(formatter.format("%20s %20s %20s", "sSeq", "oVal", "wID"));

        for (int i = 0; i < writeRecords.size(); i++) {
        	WriteRecord rec = writeRecords.get(i);
        	writer.println(formatter.format("%20s %20s %20s",
        			String.valueOf(rec.sSeq),
        			String.valueOf(rec.oVal),
					String.valueOf(rec.wId)));
        }
        
        // Close writer 
        formatter.close();
        writer.close();
	}
	
	public void writeReaderLog(String id) {
		PrintWriter writer = null;
        try {
            writer = new PrintWriter("logs" + File.separator + "log" + id + ".txt", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        writer.println("Client type : Reader");
        writer.println("Client Name : " + id);
        Formatter formatter = new Formatter();
        writer.println(formatter.format("%20s %20s %20s", "rSeq", "sSeq",  "oVal"));

        for (int i = 0; i < readRecords.size(); i++) {
        	ReadRecord rec = readRecords.get(i);
        	writer.println(formatter.format("%20s %20s %20s",
        			String.valueOf(rec.rSeq),
        			String.valueOf(rec.sSeq),
        			String.valueOf(rec.oVal)));
        }
        formatter.close();
        writer.close();
	}
	
	public void writeWriterLog(String id) {
		PrintWriter writer = null;
        try {
            writer = new PrintWriter("logs" + File.separator + "log" + id + ".txt", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        writer.println("Client type : Writer");
        writer.println("Client Name : " + id);
        Formatter formatter = new Formatter();
        writer.println(formatter.format("%20s %20s", "rSeq", "sSeq"));

        for (int i = 0; i < writeRecords.size(); i++) {
        	WriteRecord rec = writeRecords.get(i);
        	writer.println(formatter.format("%20s %20s",
        			String.valueOf(rec.rSeq),
        			String.valueOf(rec.sSeq)));
        }
        formatter.close();
        writer.close();
	}
}
