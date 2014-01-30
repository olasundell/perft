package se.atrosys.perft.cli;

import se.atrosys.perft.common.comm.NodeToHubRequestSender;

public class Main {
	public static void main(String[] args) {
		new NodeToHubRequestSender("localhost", 7800);
	}
}
