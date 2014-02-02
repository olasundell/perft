package se.atrosys.perft.node.work.worker;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import se.atrosys.perft.common.work.ResultItem;
import se.atrosys.perft.common.work.WorkItem;

public class TrueSpiderWorker extends AbstractWorker {
	public TrueSpiderWorker(PoolingHttpClientConnectionManager connectionManager) {
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
				return new TrueSpiderWorker(connectionManager);
			}
		};
	}
}
