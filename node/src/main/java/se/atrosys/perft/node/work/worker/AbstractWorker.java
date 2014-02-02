package se.atrosys.perft.node.work.worker;

import org.apache.http.HttpEntity;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;

public abstract class AbstractWorker implements Worker {
	protected final StringBuilder builder = new StringBuilder();
	protected CloseableHttpClient httpClient;
	Logger logger = LoggerFactory.getLogger(this.getClass());

	public AbstractWorker(CloseableHttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public AbstractWorker() {
		httpClient = HttpClients.createDefault();
	}

	public AbstractWorker(HttpClientConnectionManager manager) {
		httpClient = HttpClients.custom()
				.setConnectionManager(manager)
				.build();
	}

	@Override
	public void setHttpClient(CloseableHttpClient httpClient) {
		this.httpClient = httpClient;
	}

	protected void extractBody(StringBuilder builder, HttpEntity entity) throws IOException {
		InputStreamReader in = new InputStreamReader(entity.getContent());

		int str;
		char buf[] = new char[1024];

		while ( (str = in.read(buf)) != -1) {
			builder.append(buf, 0, str);
		}
		in.close();
	}
}
