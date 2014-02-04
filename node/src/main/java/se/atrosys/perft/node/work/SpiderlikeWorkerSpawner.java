package se.atrosys.perft.node.work;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.Subscriptions;
import rx.util.functions.Action0;
import rx.util.functions.Action1;
import rx.util.functions.Func1;
import se.atrosys.perft.common.work.ResultItem;
import se.atrosys.perft.common.work.WorkItem;
import se.atrosys.perft.common.work.config.WorkerConfig;
import se.atrosys.perft.node.comm.NodeToHubRequestSender;
import se.atrosys.perft.node.work.worker.SpiderMonkeyWorker;
import se.atrosys.perft.node.work.worker.Worker;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SpiderlikeWorkerSpawner extends AbstractWorkerSpawner {
	public SpiderlikeWorkerSpawner(WorkerConfig workerConfig) {
		super(workerConfig);
	}

	@Override
	public List<ResultItem> workOnItems() {
		return null;
	}

	public List<ResultItem> testWorkStuff(List<WorkItem> incomingWorkItems) {
		final PublishSubject<WorkItem> itemPublishSubject = PublishSubject.create();

		final List<ResultItem> resultItems = new ArrayList<ResultItem>();
		final BlockingQueue<WorkItem> workItems = new ArrayBlockingQueue<WorkItem>(50);
		final BlockingQueue<Worker> workers = new ArrayBlockingQueue<Worker>(5);
		final AtomicInteger count = new AtomicInteger(10);

		itemPublishSubject.subscribe(new Observer<WorkItem>() {
			@Override
			public void onCompleted() {
				logger.info("Finished!");
			}

			@Override
			public void onError(Throwable e) {
				logger.error("Error!", e);
			}

			@Override
			public void onNext(WorkItem workItem) {
				logger.info("Running {}", workItem.getUri().toString());
				workItem.prepBeforeRun();

				SpiderMonkeyWorker worker = new SpiderMonkeyWorker(connectionManager);
				resultItems.add(worker.work(workItem));

				logger.info("Finished running {}", workItem.getUri().toString());

				if (count.decrementAndGet() == 0) {
					logger.info("count is {}, exiting", count.get());
					itemPublishSubject.onCompleted();
				}

				if (worker.getNextUri() != null) {
					logger.info("Re-emitting {}", worker.getNextUri());
					WorkItem v = new WorkItem(worker.getNextUri());
					v.prepBeforeRun();
					itemPublishSubject.onNext(v);
				}
			}
		});

		for (WorkItem workItem: incomingWorkItems) {
			itemPublishSubject.onNext(workItem);
		}


//		Thread t = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					while (!workItems.isEmpty()) {
//						logger.info("Running!");
//						WorkItem workItem = workItems.take();
//						SpiderMonkeyWorker worker = new SpiderMonkeyWorker(connectionManager);
//						workers.offer(worker);
//						ResultItem work = worker.work(workItem);
//						resultItems.add(work);
//						WorkItem e = new WorkItem(worker.getNextUri());
//						e.prepBeforeRun();
//						if (count.decrementAndGet() > 0) {
//							workItems.put(e);
//						}
//						workers.remove(worker);
//					}
//				} catch (InterruptedException e) {
//					logger.error("Interrupted!", e);
//				}
//			}
//		});
//
//		t.start();
//		try {
//			t.join();
//		} catch (InterruptedException e) {
//			logger.error("Interrupted", e);
//		}

//		ExecutorService executor = Executors.newFixedThreadPool(workerConfig.getNoOfWorkers());

		workerConfig.getWorkItems();
		return resultItems;
	}
}
