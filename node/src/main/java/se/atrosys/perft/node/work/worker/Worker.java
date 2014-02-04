package se.atrosys.perft.node.work.worker;

import org.apache.http.impl.client.CloseableHttpClient;
import se.atrosys.perft.common.work.ResultItem;
import se.atrosys.perft.common.work.WorkItem;

public interface Worker {
	void setHttpClient(CloseableHttpClient httpClient);

	ResultItem work(WorkItem workItem);
}
