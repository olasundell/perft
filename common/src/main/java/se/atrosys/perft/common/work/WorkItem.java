package se.atrosys.perft.common.work;

import org.apache.http.client.methods.HttpGet;

import java.io.Serializable;
import java.net.URI;

public class WorkItem implements Serializable {
	private final URI uri;
	private HttpGet httpGet = null;

	public WorkItem(URI uri) {
		this.uri = uri;
	}

	public URI getUri() {
		return uri;
	}

	public HttpGet getHttpGet() {
		if (httpGet == null) {
			prepBeforeRun();
		}

		return httpGet;
	}

	public void prepBeforeRun() {
		httpGet = new HttpGet(uri);
	}
}
