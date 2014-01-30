package se.atrosys.perft.common;

public class HubToNodeRequest extends Request {
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
