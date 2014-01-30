package se.atrosys.perft.common;

import org.apache.http.client.methods.HttpGet;

import java.io.Serializable;
import java.net.URI;

public class WorkItem implements Serializable {
	private final URI uri;

	public WorkItem(URI uri) {
		this.uri = uri;
	}

	public URI getUri() {
		return uri;
	}

	public HttpGet getHttpGet() {
		return new HttpGet(uri);
	}
}
