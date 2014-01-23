package se.atrosys.perft.client;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
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

	public Result work(WorkItem workItem) {
		Result result = new Result();

		try {
			logger.info(String.format("Getting %s", workItem.getUri().toString()));

			StringBuilder builder = new StringBuilder();
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(workItem.getUri());

			long startTime = System.currentTimeMillis();
			CloseableHttpResponse response1 = httpClient.execute(httpGet);
			HttpEntity entity = response1.getEntity();

			extractBody(builder, entity);

			long endTime = System.currentTimeMillis();

			result.setTime(new Duration(startTime, endTime).getMillis());
			result.setStatusCode(response1.getStatusLine().getStatusCode());

			logger.debug("Finished!");
			logger.trace(builder.toString());

			response1.close();
			httpGet.completed();
			httpClient.close();
		} catch (IOException e) {
			logger.error("Could not get content from URL", e);
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
