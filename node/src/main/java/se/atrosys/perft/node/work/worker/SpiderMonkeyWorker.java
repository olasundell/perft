package se.atrosys.perft.node.work.worker;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import se.atrosys.perft.common.work.ResultItem;
import se.atrosys.perft.common.work.WorkItem;

/**
 * A Spider/Monkey worker will fetch the requested URI and suggest a follow-up to the spawner.
 */
public class SpiderMonkeyWorker extends AbstractWorker {
	public SpiderMonkeyWorker(PoolingHttpClientConnectionManager connectionManager) {
		super(connectionManager);
	}

	@Override
	public ResultItem work(WorkItem workItem) {
		return null;
	}

	@Override
	public WorkerFactory getFactoryInstance() {
		return new WorkerFactory() {
			@Override
			public Worker createWorker(PoolingHttpClientConnectionManager connectionManager) {
				return new SpiderMonkeyWorker(connectionManager);
			}
		};
	}
}
