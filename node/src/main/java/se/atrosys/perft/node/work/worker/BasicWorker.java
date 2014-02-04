package se.atrosys.perft.node.work.worker;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import se.atrosys.perft.common.work.ResultItem;
import se.atrosys.perft.common.work.WorkItem;

import java.io.IOException;

public class BasicWorker extends AbstractWorker {

	public BasicWorker(PoolingHttpClientConnectionManager connectionManager) {
		super(connectionManager);
	}

	public BasicWorker() {
		super();
	}

	/**
	 * In the Basic Worker case, this does nothing.
	 * @param builder
	 * @param resultItem
	 */

	@Override
	protected void doActualWork(StringBuilder builder, ResultItem resultItem) {

	}

}
