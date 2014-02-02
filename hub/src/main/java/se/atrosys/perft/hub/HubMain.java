package se.atrosys.perft.hub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.atrosys.perft.common.comm.NodeInfo;
import se.atrosys.perft.common.work.ResultItem;
import se.atrosys.perft.common.work.WorkItem;
import se.atrosys.perft.common.work.config.WorkerConfig;
import se.atrosys.perft.hub.comm.HubServer;
import se.atrosys.perft.hub.workproduction.WorkItemListFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

// TODO spider/monkey
// TODO picky spider/monkey
// TODO true spider
public class HubMain {
	public static boolean finished = false;
	public static Map<NodeInfo, List<ResultItem>> results = new ConcurrentHashMap<NodeInfo, List<ResultItem>>();
	private static AtomicInteger nextId = new AtomicInteger(0);

	public static void main(String[] args) throws IOException, InterruptedException {
		Logger logger = LoggerFactory.getLogger(HubMain.class);
		WorkerConfig workerConfig = new WorkerConfig.Builder()
				.noOfWorkers(50)
				.targetHostname("localhost")
				.targetPort(80)
				.prependToPath("/liveapi")
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

	public static boolean putResults(NodeInfo nodeInfo, List<ResultItem> resultList) {
		results.put(nodeInfo, resultList);

		// TODO a REALLY ugly way of saying "shut down!".
		return results.size() == nextId.get();
	}
}
