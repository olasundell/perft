package se.atrosys.perft.node.work;

import org.apache.http.HttpHost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.atrosys.perft.common.work.ResultItem;
import se.atrosys.perft.common.work.config.WorkerConfig;
import se.atrosys.perft.common.work.config.WorkerType;
import se.atrosys.perft.node.work.worker.*;

import java.util.List;

public abstract class AbstractWorkerSpawner {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final PoolingHttpClientConnectionManager connectionManager;
	protected final CloseableHttpClient httpClient;
	protected final WorkerConfig workerConfig;
	protected WorkerFactory workerFactory;

	public AbstractWorkerSpawner(WorkerConfig workerConfig) {
		connectionManager = new PoolingHttpClientConnectionManager();
		httpClient = HttpClients.custom()
				.setConnectionManager(connectionManager)
				.build();
		workerFactory = createWorkerFactory(workerConfig.getWorkerType());
		this.workerConfig = workerConfig;

		// TODO this stuff should be configurable and not hard-coded
		connectionManager.setMaxTotal(200);
		connectionManager.setDefaultMaxPerRoute(20);
		HttpHost localhost = new HttpHost(workerConfig.getTargetHostname(), workerConfig.getTargetPort());
		connectionManager.setMaxPerRoute(new HttpRoute(localhost), 50);
	}

	protected WorkerFactory createWorkerFactory(WorkerType workerType) {
		switch (workerType) {
		case BASIC:
			return new WorkerFactory() {
				@Override
				public Worker createWorker(PoolingHttpClientConnectionManager connectionManager) {
					return new BasicWorker(connectionManager);
				}
			};
		case SPIDER_MONKEY:
			return new WorkerFactory() {
				@Override
				public Worker createWorker(PoolingHttpClientConnectionManager connectionManager) {
					return new SpiderMonkeyWorker(connectionManager);
				}
			};
		case TRUE_SPIDER:
			return new WorkerFactory() {
				@Override
				public Worker createWorker(PoolingHttpClientConnectionManager connectionManager) {
					return new TrueSpiderWorker(connectionManager);
				}
			};
		default:
			logger.error("Could not create a worker with worker type {}", workerType);
		}

		return null;
	}

	public abstract List<ResultItem> workOnItems();
}
