package se.atrosys.perft.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.atrosys.perft.client.Worker;
import se.atrosys.perft.client.WorkerSpawner;
import se.atrosys.perft.common.Result;
import se.atrosys.perft.common.WorkItem;
import se.atrosys.perft.common.WorkerConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
	public static boolean finished = false;

	public static void main(String[] args) throws IOException, InterruptedException {
		Logger logger = LoggerFactory.getLogger(Main.class);
		WorkerConfig workerConfig = new WorkerConfig.WorkerConfigBuilder()
				.withNoOfWorkers(50)
				.withTargetHostname("localhost")
				.withTargetPort(80)
				.withPrependToPath("/liveapi")
				.build();

		List<WorkItem> workItems = new WorkItemListFactory().produceWorkItems("accesslog2", workerConfig);
		workerConfig.addWorkItems(workItems);

		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 7800;
		}
		try {
			new WorkerServer(port).withWorkerConfig(workerConfig).run();
		} catch (Exception e) {
			logger.error("Error!", e);
		}

		List<Result> results = new ArrayList<Result>();

//		results.addAll(new WorkerSpawner().workOnItems(workItems, 100));
//
//		int count = 0;
//
//		while (!finished && count++ < 1000) {
//			Thread.sleep(100);
//		}

//		new ResultSummarizer().summarize(results);
	}
}
