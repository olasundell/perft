package se.atrosys.perft.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.atrosys.perft.common.Operation;

public class Client {
	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger(Client.class);

//		String host = args[0];
//		int port = Integer.parseInt(args[1]);

		String host = "localhost";
		int port = 7800;

		new WorkerClient(host, port).getWork();
	}
}
