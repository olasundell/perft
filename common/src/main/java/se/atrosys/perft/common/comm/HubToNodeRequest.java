package se.atrosys.perft.common.comm;

import java.io.Serializable;

public class HubToNodeRequest extends Request implements Serializable {
	private int clientId;

	protected HubToNodeRequest(Operation operation) {
		super(operation);
	}

	public HubToNodeRequest(Operation operation, int clientId) {
		this(operation);
		this.clientId = clientId;
	}

	public int getClientId() {
		return clientId;
	}
}
