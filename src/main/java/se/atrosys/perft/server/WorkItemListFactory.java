package se.atrosys.perft.server;

import se.atrosys.perft.common.WorkItem;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class WorkItemListFactory {
	public List<WorkItem> produceWorkItems() throws IOException {
		List<WorkItem> workItems = new ArrayList<WorkItem>();

		AccessLogConverter accessLogConverter = new AccessLogConverter();
		WorkItemFactory workItemFactory = new WorkItemFactory();

		for (AccessLogConverter.AccessLogItem item: accessLogConverter.parseLog("accesslog")) {
			workItems.add(workItemFactory.convert(item));
		}

		return workItems;
	}

	class WorkItemFactory {

		public WorkItem convert(AccessLogConverter.AccessLogItem item) {
			URI uri = URI.create(item.getRequest());
			if (!uri.isAbsolute()) {
				// TODO this should be configurable
				uri = URI.create("http://localhost:8080" + uri.toString());
			}
			return new WorkItem(uri);
		}
	}
}
