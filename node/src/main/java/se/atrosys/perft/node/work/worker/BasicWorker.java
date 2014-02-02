package se.atrosys.perft.node.work.worker;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import se.atrosys.perft.common.work.ResultItem;
import se.atrosys.perft.common.work.WorkItem;

import java.io.IOException;

public class BasicWorker extends AbstractWorker {

	public BasicWorker(CloseableHttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public BasicWorker(HttpClientConnectionManager manager) {
		httpClient = HttpClients.custom()
				.setConnectionManager(manager)
				.build();
	}

	public BasicWorker() {
		httpClient = HttpClients.createDefault();
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

}
