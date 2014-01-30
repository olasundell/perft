package se.atrosys.perft.common;

import java.io.Serializable;

public class Request implements Serializable {
	protected Operation operation;

	protected Request(Operation operation) {
		this.operation = operation;
	}

	public Operation getOperation() {
		return operation;
	}
}
