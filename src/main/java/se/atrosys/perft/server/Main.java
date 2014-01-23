package se.atrosys.perft.server;

import se.atrosys.perft.client.Worker;
import se.atrosys.perft.common.Result;
import se.atrosys.perft.common.WorkItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
	public static void main(String[] args) throws IOException {
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

		List<WorkItem> workItems = new WorkItemListFactory().produceWorkItems();
		List<Result> results = new ArrayList<Result>();

		for (WorkItem item: workItems) {
			results.add(new Worker().work(item));
		}

		new ResultSummarizer().summarize(results);
	}
}
