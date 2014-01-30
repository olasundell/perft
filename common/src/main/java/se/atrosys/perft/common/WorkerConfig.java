package se.atrosys.perft.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WorkerConfig implements Serializable {
	private List<WorkItem> workItems;
	private int noOfWorkers;
	private String targetHostname;
	private int targetPort;
	private String prependToPath;
	private int workerId;

	public WorkerConfig() {
		this.workItems = new ArrayList<WorkItem>();
		this.prependToPath = "";
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

	public static class WorkerConfigBuilder {
		private final WorkerConfig workerConfig;

		public WorkerConfigBuilder() {
			this.workerConfig = new WorkerConfig();
		}

		public WorkerConfig build() {
			return workerConfig;
		}

		public WorkerConfigBuilder withWorkItems(List<WorkItem> workItems) {
			workerConfig.workItems = workItems;

			return this;
		}

		public WorkerConfigBuilder withNoOfWorkers(int noOfWorkers) {
			workerConfig.noOfWorkers = noOfWorkers;

			return this;
		}

		public WorkerConfigBuilder withTargetHostname(String targetHostname) {
			workerConfig.targetHostname = targetHostname;

			return this;
		}

		public WorkerConfigBuilder withTargetPort(int targetPort) {
			workerConfig.targetPort = targetPort;

			return this;
		}

		public WorkerConfigBuilder withPrependToPath(String prependToPath) {
			workerConfig.prependToPath = prependToPath;

			return this;
		}
	}
}
