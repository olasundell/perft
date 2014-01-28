package se.atrosys.perft.server;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.atrosys.perft.common.WorkItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WorkItemListFactoryTest {

	private WorkItemListFactory factory;

	@BeforeMethod
	public void setUp() throws Exception {
		factory = new WorkItemListFactory("accesslog");
	}

	@Test
	public void shouldProduceEmptyList() throws Exception {
		factory.setAccessLogConverter(new AccessLogConverter() {
			@Override
			public List<AccessLogItem> parseLog(String fileName) throws IOException {
				return new ArrayList<AccessLogItem>();
			}
		});

		List<WorkItem> list = factory.produceWorkItems();

		Assert.assertNotNull(list);
		Assert.assertEquals(list.size(), 0);
	}
}
