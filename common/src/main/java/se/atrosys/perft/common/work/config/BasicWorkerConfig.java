package se.atrosys.perft.common.work.config;

public class BasicWorkerConfig extends WorkerConfig {
	protected BasicWorkerConfig(Builder builder) {
		super(builder);
	}

	@Override
	public WorkerType getWorkerType() {
		return WorkerType.BASIC;
	}
}
