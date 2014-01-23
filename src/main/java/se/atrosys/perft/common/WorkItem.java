package se.atrosys.perft.common;

import java.net.URI;

public class WorkItem {
	private final URI uri;

	public WorkItem(URI uri) {
		this.uri = uri;
	}

	public URI getUri() {
		return uri;
	}
}
