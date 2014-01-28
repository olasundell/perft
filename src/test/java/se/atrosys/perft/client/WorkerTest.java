package se.atrosys.perft.client;

import mockit.Expectations;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.atrosys.perft.common.Result;
import se.atrosys.perft.common.WorkItem;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Locale;

public class WorkerTest {
	public static final int STATUS_CODE = 200;
	@Mocked CloseableHttpClient httpClient;
	@Mocked CloseableHttpResponse response;
	@Mocked HttpEntity entity;
	@Mocked StatusLine statusLine;

	private WorkItem workItem;
	private Worker worker;
	private Result result;

	@BeforeClass
	public void setupMockedStuff() throws IOException {
	}

	@BeforeMethod
	public void setupWorker() throws IOException {
		new NonStrictExpectations() {{
			httpClient.execute(withAny(new HttpGet(anyString))); result = response;
			response.getEntity(); result = entity;
			entity.getContent(); result = new ByteArrayInputStream("foobar".getBytes());
			response.getStatusLine(); result = statusLine;
			statusLine.getStatusCode(); result = STATUS_CODE;
			response.close(); times = 1;
		}};

		workItem = new WorkItem(URI.create("http://localhost:8080"));
		worker = new Worker();
		worker.setHttpClient(httpClient);
		result = worker.work(workItem);
	}

	@Test
	public void shouldHaveResults() {
		Assert.assertNotNull(result);
	}

	@Test
	public void shouldHaveTimeInResults() {
		Assert.assertNotSame(result.getTime(), 0);
	}

	@Test
	public void shouldHaveStatusCodeInResults() {
		Assert.assertEquals(result.getStatusCode(), STATUS_CODE);
	}

}
