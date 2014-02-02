package se.atrosys.perft.common.work.config;

public class SpiderMonkeyConfig extends WorkerConfig {
	private int numberOfIterations;

	private SpiderMonkeyConfig(Builder builder) {
		super(builder);
		numberOfIterations = builder.numberOfIterations;
	}

	@Override
	public WorkerType getWorkerType() {
		return WorkerType.SPIDER_MONKEY;
	}


	public static final class Builder extends WorkerConfig.Builder {
		private int numberOfIterations;

		public Builder() {
		}

		public Builder(SpiderMonkeyConfig copy) {
			numberOfIterations = copy.numberOfIterations;
		}

		public Builder numberOfIterations(int numberOfIterations) {
			this.numberOfIterations = numberOfIterations;
			return this;
		}

		public SpiderMonkeyConfig build() {
			return new SpiderMonkeyConfig(this);
		}
	}
}
