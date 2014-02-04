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

public class BasicWorkerSpawner extends AbstractWorkerSpawner {

	public BasicWorkerSpawner(WorkerConfig workerConfig) {
		super(workerConfig);
	}

	public List<ResultItem> workOnItems() {
		Scheduler scheduler = Schedulers.executor(Executors.newFixedThreadPool(workerConfig.getNoOfWorkers()));
		final List<ResultItem> resultItems = new Vector<ResultItem>();

		Action1<Throwable> onError = new Action1<Throwable>() {
			@Override
			public void call(Throwable throwable) {
				logger.warn("Failure", throwable);
			}
		};

		Action1<WorkItem> onNext = new Action1<WorkItem>() {
			@Override
			public void call(WorkItem workItem) {
				Worker worker = workerFactory.createWorker(connectionManager);
				ResultItem result = worker.work(workItem);
				resultItems.add(result);
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
