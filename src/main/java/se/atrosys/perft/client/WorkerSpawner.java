package se.atrosys.perft.client;

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
import se.atrosys.perft.common.Result;
import se.atrosys.perft.common.WorkItem;
import se.atrosys.perft.common.WorkerConfig;
import se.atrosys.perft.server.Main;
import se.atrosys.perft.server.ResultSummarizer;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkerSpawner {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final PoolingHttpClientConnectionManager connectionManager;
	private final CloseableHttpClient httpClient;
	private final WorkerConfig workerConfig;

	public WorkerSpawner(WorkerConfig workerConfig) {
		this.workerConfig = workerConfig;
		connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(200);
		connectionManager.setDefaultMaxPerRoute(20);
		HttpHost localhost = new HttpHost(workerConfig.getTargetHostname(), workerConfig.getTargetPort());
		connectionManager.setMaxPerRoute(new HttpRoute(localhost), 50);

		httpClient = HttpClients.custom()
				.setConnectionManager(connectionManager)
				.build();
	}

	public List<Result> workOnItems() {
		Scheduler scheduler = Schedulers.executor(Executors.newFixedThreadPool(workerConfig.getNoOfWorkers()));
		final List<Result> results = new Vector<Result>();
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
				results.add(new Worker(connectionManager).work(workItem));
			}
		};

		Action0 onComplete = new Action0() {
			@Override
			public void call() {
				logger.info("Completed!");


//				new ResultSummarizer().summarize(results);
//				Main.finished = true;
			}
		};

		rx.Observable.from(workerConfig.getWorkItems()).subscribe(onNext, onError, onComplete, scheduler);

		return results;
	}
}
