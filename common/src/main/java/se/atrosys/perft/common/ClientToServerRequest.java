package se.atrosys.perft.common;

public class ClientToServerRequest {
	private final Operation operation;
	private final int workerId;

	public ClientToServerRequest(Operation operation, int workerId) {
		this.operation = operation;
		this.workerId = workerId;
	}

	public Operation getOperation() {
		return operation;
	}

	public int getWorkerId() {
		return workerId;
	}
}
