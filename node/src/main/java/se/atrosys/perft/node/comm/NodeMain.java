package se.atrosys.perft.node.comm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeMain {
	private static int id = 0;

	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger(NodeMain.class);

		String host = "localhost";
		int port = 7800;

		NodeToHubRequestSender hubRequestSender = new NodeToHubRequestSender(host, port);
		hubRequestSender.register();
	}

	public static int getId() {
		return id;
	}

	public static void setId(int id) {
		NodeMain.id = id;
	}
}
