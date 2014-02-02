package se.atrosys.perft.node.work;

import org.apache.http.HttpHost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.*;
import rx.schedulers.Schedulers;
import rx.util.functions.Action0;
import rx.util.functions.Action1;
import se.atrosys.perft.common.work.ResultItem;
import se.atrosys.perft.common.work.WorkItem;
import se.atrosys.perft.common.work.config.WorkerConfig;
import se.atrosys.perft.common.work.config.WorkerType;
import se.atrosys.perft.node.comm.NodeToHubRequestSender;
import se.atrosys.perft.node.work.worker.*;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkerSpawner {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final PoolingHttpClientConnectionManager connectionManager;
	private final CloseableHttpClient httpClient;
	private final WorkerConfig workerConfig;
	private WorkerFactory workerFactory;

	public WorkerSpawner(WorkerConfig workerConfig) {
		this.workerConfig = workerConfig;
		connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(200);
		connectionManager.setDefaultMaxPerRoute(20);
		HttpHost localhost = new HttpHost(workerConfig.getTargetHostname(), workerConfig.getTargetPort());
		connectionManager.setMaxPerRoute(new HttpRoute(localhost), 50);

		workerFactory = createWorkerFactory(workerConfig.getWorkerType());

		httpClient = HttpClients.custom()
				.setConnectionManager(connectionManager)
				.build();
	}

	private WorkerFactory createWorkerFactory(WorkerType workerType) {
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

	public List<ResultItem> workOnItems() {
		Scheduler scheduler = Schedulers.executor(Executors.newFixedThreadPool(workerConfig.getNoOfWorkers()));
		final List<ResultItem> resultItems = new Vector<ResultItem>();
		final AtomicInteger count = new AtomicInteger(0);

		Action1<Throwable> onError = new Action1<Throwable>() {
			@Override
			public void call(Throwable throwable) {
				logger.warn("Failure", throwable);
			}
		};

		Action1<WorkItem> onNext = new Action1<WorkItem>() {
			@Override
			public void call(WorkItem workItem) {
				resultItems.add(workerFactory.createWorker(connectionManager).work(workItem));
			}
		};

		Action0 onComplete = new Action0() {
			@Override
			public void call() {
				logger.info("Completed, sending resultItems.");
				// TODO hard-coded server config
				new NodeToHubRequestSender("localhost", 7800).sendResults(resultItems);
			}
		};

		rx.Observable.from(workerConfig.getWorkItems()).subscribe(onNext, onError, onComplete, scheduler);

		return resultItems;
	}
}
