package se.atrosys.perft.common.work.config;

import se.atrosys.perft.common.work.WorkItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class WorkerConfig implements Serializable {
	private List<WorkItem> workItems;
	private int noOfWorkers;
	private String targetHostname;
	private int targetPort;
	private String prependToPath;
	private int workerId;

//	public WorkerConfig() {
//		this.workItems = new ArrayList<WorkItem>();
//		this.prependToPath = "";
//	}

	protected WorkerConfig(Builder builder) {
		setWorkItems(builder.workItems);
		noOfWorkers = builder.noOfWorkers;
		targetHostname = builder.targetHostname;
		targetPort = builder.targetPort;
		prependToPath = builder.prependToPath;
		setWorkerId(builder.workerId);
	}

	public List<WorkItem> getWorkItems() {
		return workItems;
	}

	public int getNoOfWorkers() {
		return noOfWorkers;
	}

	public String getTargetHostname() {
		return targetHostname;
	}

	public int getTargetPort() {
		return targetPort;
	}

	public void addWorkItems(List<WorkItem> workItems) {
		this.workItems.addAll(workItems);
	}

	public String getPrependToPath() {
		return prependToPath;
	}

	public int getWorkerId() {
		return workerId;
	}

	public void setWorkerId(int workerId) {
		this.workerId = workerId;
	}

	public void setWorkItems(List<WorkItem> workItems) {
		this.workItems = workItems;
	}

	public abstract WorkerType getWorkerType();

	public static class Builder {
		private List<WorkItem> workItems = new ArrayList<WorkItem>();
		private int noOfWorkers;
		private String targetHostname;
		private int targetPort;
		private String prependToPath;
		private int workerId;

		public Builder() {
		}

		public Builder(WorkerConfig copy) {
			workItems = copy.workItems;
			noOfWorkers = copy.noOfWorkers;
			targetHostname = copy.targetHostname;
			targetPort = copy.targetPort;
			prependToPath = copy.prependToPath;
			workerId = copy.workerId;
		}

		public Builder workItems(List<WorkItem> workItems) {
			this.workItems = workItems;
			return this;
		}

		public Builder noOfWorkers(int noOfWorkers) {
			this.noOfWorkers = noOfWorkers;
			return this;
		}

		public Builder targetHostname(String targetHostname) {
			this.targetHostname = targetHostname;
			return this;
		}

		public Builder targetPort(int targetPort) {
			this.targetPort = targetPort;
			return this;
		}

		public Builder prependToPath(String prependToPath) {
			this.prependToPath = prependToPath;
			return this;
		}

		public Builder workerId(int workerId) {
			this.workerId = workerId;
			return this;
		}

		public WorkerConfig build() {
			return new BasicWorkerConfig(this);
		}
	}


//	public static class WorkerConfigBuilder {
//		private final WorkerConfig workerConfig;
//
//		public WorkerConfigBuilder() {
//			this.workerConfig = new BasicWorkerConfig();
//		}
//
//		public WorkerConfig build() {
//			return workerConfig;
//		}
//
//		public WorkerConfigBuilder withWorkItems(List<WorkItem> workItems) {
//			workerConfig.workItems = workItems;
//
//			return this;
//		}
//
//		public WorkerConfigBuilder withNoOfWorkers(int noOfWorkers) {
//			workerConfig.noOfWorkers = noOfWorkers;
//
//			return this;
//		}
//
//		public WorkerConfigBuilder withTargetHostname(String targetHostname) {
//			workerConfig.targetHostname = targetHostname;
//
//			return this;
//		}
//
//		public WorkerConfigBuilder withTargetPort(int targetPort) {
//			workerConfig.targetPort = targetPort;
//
//			return this;
//		}
//
//		public WorkerConfigBuilder withPrependToPath(String prependToPath) {
//			workerConfig.prependToPath = prependToPath;
//
//			return this;
//		}
//	}
}
