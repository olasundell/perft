package se.atrosys.perft.common;

import java.io.Serializable;

public class NodeToHubRequest extends Request {
	private final int workerId;

	public NodeToHubRequest(Operation operation, int workerId) {
		super(operation);
		this.workerId = workerId;
	}

	public int getWorkerId() {
		return workerId;
	}
}
