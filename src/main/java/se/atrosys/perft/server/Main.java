package se.atrosys.perft.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.atrosys.perft.common.ResultItem;
import se.atrosys.perft.common.WorkItem;
import se.atrosys.perft.common.WorkerConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
	public static boolean finished = false;
	public static Map<Integer, List<ResultItem>> results = new ConcurrentHashMap<Integer, List<ResultItem>>();

	public static void main(String[] args) throws IOException, InterruptedException {
		Logger logger = LoggerFactory.getLogger(Main.class);
		WorkerConfig workerConfig = new WorkerConfig.WorkerConfigBuilder()
				.withNoOfWorkers(50)
				.withTargetHostname("localhost")
				.withTargetPort(80)
				.withPrependToPath("/liveapi")
				.build();

		List<WorkItem> workItems = new WorkItemListFactory().produceWorkItems("accesslog", workerConfig);
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

//		int count = 0;
//
//		while (!finished && count++ < 40) {
//			logger.info("Waiting for results.");
//			Thread.sleep(500);
//		}

		new ResultSummarizer().summarize(results.get(0));
	}
}
