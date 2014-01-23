package se.atrosys.perft.client;

import org.junit.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.atrosys.perft.common.Result;
import se.atrosys.perft.common.WorkItem;

import java.net.MalformedURLException;
import java.net.URI;

public class WorkerTest {

	private WorkItem workItem;
	private Worker worker;
	private Result result;

	@BeforeMethod
	public void setupWorker() {
		workItem = new WorkItem(URI.create("http://localhost:8080"));
		worker = new Worker();
		result = worker.work(workItem);
	}
	@Test
	public void shouldReturnContent() throws MalformedURLException {
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
		Assert.assertNotSame(result.getStatusCode(), 0);
	}
}
