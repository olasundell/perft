package se.atrosys.perft.node.work.worker;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.atrosys.perft.common.work.ResultItem;

import java.net.URI;
import java.util.List;

public class SpiderMonkeyWorkerTest {
	private SpiderMonkeyWorker worker;

	@BeforeMethod
	public void createInstance() {
		worker = new SpiderMonkeyWorker(null);
	}

	@Test
	public void shouldReturnEmptyList() {
		StringBuilder builder = new StringBuilder();
		builder.append("<html></html>");
		List<URI> list = worker.findWorthwhileURIs(builder);

		Assert.assertNotNull(list);
		Assert.assertEquals(list.size(), 0);
	}

	@Test
	public void shouldReturnNonEmptyList() {
		StringBuilder builder = new StringBuilder();
		URI uri = URI.create("http://localhost:8080/foo/");
		builder.append("<html>");
		builder.append("<body>");
		builder.append("<a href=\"");
		builder.append(uri.toString());
		builder.append("\"></a>");
		builder.append("</body>");
		builder.append("</html>");
		List<URI> list = worker.findWorthwhileURIs(builder);

		Assert.assertNotNull(list);
		Assert.assertEquals(list.size(), 1);
		URI resultUri = list.get(0);
		Assert.assertEquals(resultUri.compareTo(uri), 0,
				String.format("Expected %s, got %s",
						uri.toString(),
						resultUri.toString()));
	}

	@Test
	public void testWorkNext() {
		worker.doActualWork(new StringBuilder(), new ResultItem());
	}
}
