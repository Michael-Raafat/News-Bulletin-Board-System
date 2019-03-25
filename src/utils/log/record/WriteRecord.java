package utils.log.record;

public class WriteRecord implements Record {
	public int sSeq;
	public int rSeq;
	public int oVal;
	public int wId;
	
	public WriteRecord(int sSeq, int rSeq, int oVal, int wId) {
		super();
		this.sSeq = sSeq;
		this.rSeq = rSeq;
		this.oVal = oVal;
		this.wId = wId;
	}
	
	public WriteRecord(String s) {
		String[] args = s.split(" ");
		sSeq = Integer.parseInt(args[0].trim());
		rSeq = Integer.parseInt(args[1].trim());
		oVal = Integer.parseInt(args[2].trim());
		wId = Integer.parseInt(args[3].trim());
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(sSeq);
		builder.append(" ");
		builder.append(rSeq);
		builder.append(" ");
		builder.append(oVal);
		builder.append(" ");
		builder.append(wId);
		builder.append("\n");
		return builder.toString();
	}
}
