package client.socket.worker;

import utils.types.RequestType;

public class WriteWorker extends Worker {
	private final static RequestType TYPE = RequestType.WRITE;
	
	public WriteWorker() {
		super(TYPE);
	}
	
	@Override
	public String generate_request_message(String id) {
		return TYPE.toString() + " " + id + " " + id;
	}

	@Override
	public void print_log(String id) {
		this.log.writeWriterLog(id);
	}


}
