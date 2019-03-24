package utils.log.record;

public class ReadRecord implements Record{
	public int sSeq;
	public int rSeq;
	public int oVal;
	public int rNum;
	public int rId;
	
	public ReadRecord(int sSeq, int rSeq, int oVal, int rNum, int rId) {
		super();
		this.sSeq = sSeq;
		this.rSeq = rSeq;
		this.oVal = oVal;
		this.rNum = rNum;
		this.rId = rId;
	}
	
	public ReadRecord(String s) {
		String[] args = s.split(" ");
		sSeq = Integer.parseInt(args[0]);
		rSeq = Integer.parseInt(args[1]);
		oVal = Integer.parseInt(args[2]);
		rNum = Integer.parseInt(args[3]);
		rId = Integer.parseInt(args[4]);
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(sSeq);
		builder.append(" ");
		builder.append(rSeq);
		builder.append(" ");
		builder.append(oVal);
		builder.append(" ");
		builder.append(rNum);
		builder.append(" ");
		builder.append(rId);
		builder.append("\n");
		return builder.toString();
	}
	
}
