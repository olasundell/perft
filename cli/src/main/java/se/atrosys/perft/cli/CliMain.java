package se.atrosys.perft.cli;

public class CliMain {
	public static void main(String[] args) {
		if (args.length != 1) {
			printUsage();
		}

		new CliToHubRequestSender("localhost", 7800).startWork(args[0]);
	}

	private static void printUsage() {
		System.out.println("Usage: <accesslog>");
	}
}
