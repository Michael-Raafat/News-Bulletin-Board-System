package client.socket.worker;

import utils.types.RequestType;

public class ReadWorker extends Worker {

	private final static RequestType TYPE = RequestType.READ;
	
	public ReadWorker() {
		super(TYPE);
	}
	
	@Override
	public String generate_request_message(String id) {
		return TYPE.toString() + " " + id;
	}

	@Override
	public void print_log(String id) {
		this.log.writeReaderLog(id);
	}


}
