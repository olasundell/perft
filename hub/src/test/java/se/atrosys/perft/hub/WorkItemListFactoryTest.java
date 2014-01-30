package se.atrosys.perft.hub;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.atrosys.perft.common.WorkItem;
import se.atrosys.perft.common.WorkerConfig;
import se.atrosys.perft.hub.workproduction.AccessLogConverter;
import se.atrosys.perft.hub.workproduction.WorkItemListFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorkItemListFactoryTest {

	public static final String RELATIVE_URL = "/foo";
	public static final String PREPEND_TO_PATH = "/ladlas";
	private WorkItemListFactory factory;
	private WorkerConfig workerConfig;

	@BeforeMethod
	public void setUp() throws Exception {
		factory = new WorkItemListFactory();
		workerConfig = new WorkerConfig.WorkerConfigBuilder()
				.withTargetHostname("localhost")
				.withTargetPort(9000)
				.withPrependToPath(PREPEND_TO_PATH)
				.build();
	}

	@Test
	public void shouldProduceEmptyList() throws IOException {
		factory.setAccessLogConverter(new AccessLogConverter() {
			@Override
			public List<AccessLogItem> parseLog(String fileName) throws IOException {
				return new ArrayList<AccessLogItem>();
			}
		});

		List<WorkItem> list = factory.produceWorkItems("accesslog", workerConfig);

		Assert.assertNotNull(list);
		Assert.assertEquals(list.size(), 0);
	}

	@Test
	public void shouldRewriteURIs() throws IOException {
		factory.setAccessLogConverter(new AccessLogConverter() {
			@Override
			public List<AccessLogItem> parseLog(String fileName) throws IOException {
				return Arrays.asList(new AccessLogItem("GET", RELATIVE_URL, "404"));
			}
		});

		List<WorkItem> list = factory.produceWorkItems("accesslog", workerConfig);

		Assert.assertEquals(list.size(), 1);
		final String uri = list.get(0).getUri().toString();
		Assert.assertNotSame(uri, RELATIVE_URL);
		Assert.assertTrue(uri.contains(PREPEND_TO_PATH));
	}
}
