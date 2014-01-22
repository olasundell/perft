package se.atrosys.perft.server;

public class Main {
	public static void main(String[] args) {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 8095;
		}
		try {
			new WorkerServer(port).run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
