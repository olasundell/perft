package se.atrosys.perft.common;

import java.net.URL;

public class WorkItem {
	private final URL url;

	public WorkItem(URL url) {
		this.url = url;
	}

	public URL getUrl() {
		return url;
	}
}
