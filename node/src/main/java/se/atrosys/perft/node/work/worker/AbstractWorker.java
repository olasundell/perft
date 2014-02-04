package se.atrosys.perft.node.work.worker;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.atrosys.perft.common.work.ResultItem;
import se.atrosys.perft.common.work.WorkItem;

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

	@Override
	public ResultItem work(WorkItem workItem) {
		ResultItem resultItem = new ResultItem();

		try {
			logger.debug(String.format("Getting %s", workItem.getUri().toString()));

			long startTime = System.currentTimeMillis();
			CloseableHttpResponse response1 = httpClient.execute(workItem.getHttpGet());
			HttpEntity entity = response1.getEntity();

			extractBody(builder, entity);

			doActualWork(builder, resultItem);

			long endTime = System.currentTimeMillis();

			resultItem.setStartTime(startTime);
			resultItem.setEndTime(endTime);
			resultItem.setStatusCode(response1.getStatusLine().getStatusCode());
			if (resultItem.getStatusCode() != 200 ) {
				logger.warn(String.format("Did a request to %s, but got status code %d",
						workItem.getUri().toString(),
						resultItem.getStatusCode()));
			}

			logger.debug("Finished!");
			logger.trace(builder.toString());

			response1.close();
			workItem.getHttpGet().completed();
		} catch (IOException e) {
			logger.error("Could not get content from URL", e);
			resultItem.markAsFailed();
		}

		return resultItem;
	}

	protected abstract void doActualWork(StringBuilder builder, ResultItem resultItem);
}
