package se.atrosys.perft.hub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.atrosys.perft.common.NodeInfo;
import se.atrosys.perft.common.ResultItem;
import se.atrosys.perft.common.WorkItem;
import se.atrosys.perft.common.WorkerConfig;
import se.atrosys.perft.hub.comm.HubServer;
import se.atrosys.perft.hub.workproduction.WorkItemListFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class HubMain {
	public static boolean finished = false;
	public static Map<NodeInfo, List<ResultItem>> results = new ConcurrentHashMap<NodeInfo, List<ResultItem>>();
	private static AtomicInteger nextId = new AtomicInteger(0);

	public static void main(String[] args) throws IOException, InterruptedException {
		Logger logger = LoggerFactory.getLogger(HubMain.class);
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
			new HubServer(port).withWorkerConfig(workerConfig).run();
		} catch (Exception e) {
			logger.error("Error!", e);
		}

		List<ResultItem> totalResults = new ArrayList<ResultItem>();

		for (List<ResultItem> list: results.values()) {
			totalResults.addAll(list);
		}

		new ResultSummarizer().summarize(totalResults);
	}

	public static int getNextId() {
		return nextId.getAndIncrement();
	}
}
