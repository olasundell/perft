package se.atrosys.perft.server;

import se.atrosys.perft.client.Worker;
import se.atrosys.perft.client.WorkerSpawner;
import se.atrosys.perft.common.Result;
import se.atrosys.perft.common.WorkItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
	public static boolean finished = false;

	public static void main(String[] args) throws IOException, InterruptedException {
//		int port;
//		if (args.length > 0) {
//			port = Integer.parseInt(args[0]);
//		} else {
//			port = 8095;
//		}
//		try {
//			new WorkerServer(port).run();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		List<WorkItem> workItems = new WorkItemListFactory("accesslog2").produceWorkItems();
		List<Result> results = new ArrayList<Result>();

		results.addAll(new WorkerSpawner().workOnItems(workItems, 100));

		int count = 0;

		while (!finished && count++ < 1000) {
			Thread.sleep(100);
		}

//		new ResultSummarizer().summarize(results);
	}
}
