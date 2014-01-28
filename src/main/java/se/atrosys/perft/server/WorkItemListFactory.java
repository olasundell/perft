package se.atrosys.perft.server;

import se.atrosys.perft.common.WorkItem;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class WorkItemListFactory {
	private final String accessLogFileName;
	private AccessLogConverter accessLogConverter = new AccessLogConverterImpl();

	public WorkItemListFactory(String accessLogFileName) {
		this.accessLogFileName = accessLogFileName;
	}

	public List<WorkItem> produceWorkItems() throws IOException {
		List<WorkItem> workItems = new ArrayList<WorkItem>();

		WorkItemFactory workItemFactory = new WorkItemFactory();

		for (AccessLogConverterImpl.AccessLogItem item: accessLogConverter.parseLog(accessLogFileName)) {
			workItems.add(workItemFactory.convert(item));
		}

		return workItems;
	}

	class WorkItemFactory {

		public WorkItem convert(AccessLogConverterImpl.AccessLogItem item) {
			URI uri = URI.create(item.getRequest());
			if (!uri.isAbsolute()) {
				// TODO this should be configurable
				uri = URI.create("http://localhost/liveapi" + uri.toString());
			}
			return new WorkItem(uri);
		}
	}

	public void setAccessLogConverter(AccessLogConverter accessLogConverter) {
		this.accessLogConverter = accessLogConverter;
	}
}
