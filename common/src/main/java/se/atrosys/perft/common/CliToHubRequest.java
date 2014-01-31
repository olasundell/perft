package se.atrosys.perft.common;

public class CliToHubRequest extends Request {
	private String filename;

	protected CliToHubRequest(Operation operation) {
		super(operation);
	}

	public CliToHubRequest(Operation operation, String filename) {
		this(operation);
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}
}
