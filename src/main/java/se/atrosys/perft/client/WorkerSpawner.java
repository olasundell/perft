package se.atrosys.perft.client;

import rx.Notification;
import rx.Scheduler;
import rx.schedulers.Schedulers;
import rx.util.functions.Action1;
import se.atrosys.perft.common.WorkItem;

import java.util.List;

public class WorkerSpawner {
	public void workOnItems(List<WorkItem> workItems, int noOfWorkers) {
		Scheduler scheduler = Schedulers.newThread();
		rx.Observable.from(workItems, scheduler).subscribe(
				new Action1<WorkItem>() {
					@Override
					public void call(WorkItem workItem) {
						new Worker().work(workItem);
					}
				}
		);
	}
}
