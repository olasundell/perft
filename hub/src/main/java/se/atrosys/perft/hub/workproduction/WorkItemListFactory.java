package se.atrosys.perft.hub.workproduction;

import se.atrosys.perft.common.work.WorkItem;
import se.atrosys.perft.common.work.config.WorkerConfig;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class WorkItemListFactory {
	private AccessLogConverter accessLogConverter = new AccessLogConverterImpl();

	public List<WorkItem> produceWorkItems(String accessLogFileName, WorkerConfig workerConfig) throws IOException {
		List<WorkItem> workItems = new ArrayList<WorkItem>();

		WorkItemFactory workItemFactory = new WorkItemFactory();

		for (AccessLogConverterImpl.AccessLogItem item: accessLogConverter.parseLog(accessLogFileName)) {
			workItems.add(workItemFactory.convert(item, workerConfig));
		}

		return workItems;
	}

	class WorkItemFactory {

		public WorkItem convert(AccessLogConverterImpl.AccessLogItem item, WorkerConfig workerConfig) {
			URI uri = URI.create(item.getRequest());
			if (!uri.isAbsolute()) {
				uri = URI.create(String.format("http://%s:%d%s%s",
						workerConfig.getTargetHostname(),
						workerConfig.getTargetPort(),
						workerConfig.getPrependToPath(),
						uri.toString()));
			}
			return new WorkItem(uri);
		}
	}

	public void setAccessLogConverter(AccessLogConverter accessLogConverter) {
		this.accessLogConverter = accessLogConverter;
	}
}
