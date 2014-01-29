package se.atrosys.perft.common;

import org.apache.http.client.methods.HttpGet;

import java.net.URI;

public class WorkItem {
	private final URI uri;
	private final HttpGet httpGet;

	public WorkItem(URI uri) {
		this.uri = uri;
		httpGet = new HttpGet(uri);
	}

	public URI getUri() {
		return uri;
	}

	public HttpGet getHttpGet() {
		return httpGet;
	}
}
