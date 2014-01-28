package se.atrosys.perft.client;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.atrosys.perft.common.Result;
import se.atrosys.perft.common.WorkItem;

import java.io.IOException;
import java.io.InputStreamReader;

public class Worker {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private final StringBuilder builder = new StringBuilder();
	private CloseableHttpClient httpClient;

	public Worker(CloseableHttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public Worker(HttpClientConnectionManager manager) {
		httpClient = HttpClients.custom()
				.setConnectionManager(manager)
				.build();
	}

	public Worker() {
		httpClient = HttpClients.createDefault();
	}

	public void setHttpClient(CloseableHttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public Result work(WorkItem workItem) {
		Result result = new Result();

		try {
			logger.debug(String.format("Getting %s", workItem.getUri().toString()));

			HttpGet httpGet = new HttpGet(workItem.getUri());

			long startTime = System.currentTimeMillis();
			CloseableHttpResponse response1 = httpClient.execute(httpGet);
			HttpEntity entity = response1.getEntity();

			extractBody(builder, entity);

			long endTime = System.currentTimeMillis();

			result.setStartTime(startTime);
			result.setEndTime(endTime);
			result.setStatusCode(response1.getStatusLine().getStatusCode());
			if (result.getStatusCode() != 200 ) {
				logger.warn(String.format("Did a request to %s, but got status code %d",
						workItem.getUri().toString(),
						result.getStatusCode()));
			}

			logger.debug("Finished!");
			logger.trace(builder.toString());

			response1.close();
			httpGet.completed();
		} catch (IOException e) {
			logger.error("Could not get content from URL", e);
			result.markAsFailed();
		}

		return result;
	}

	private void extractBody(StringBuilder builder, HttpEntity entity) throws IOException {
		InputStreamReader in = new InputStreamReader(entity.getContent());

		int str;
		char buf[] = new char[1024];

		while ( (str = in.read(buf)) != -1) {
			builder.append(buf, 0, str);
		}
		in.close();
	}
}
