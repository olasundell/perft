package se.atrosys.perft.common.comm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Node {
	private static int id = 0;

	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger(Node.class);

		String host = "localhost";
		int port = 7800;

		NodeToHubRequestSender hubRequestSender = new NodeToHubRequestSender(host, port);
		hubRequestSender.register();
	}

	public static int getId() {
		return id;
	}

	public static void setId(int id) {
		Node.id = id;
	}
}
