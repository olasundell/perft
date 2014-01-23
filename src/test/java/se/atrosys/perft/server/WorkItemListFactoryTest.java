package se.atrosys.perft.server;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.atrosys.perft.common.WorkItem;

import java.util.List;

public class WorkItemListFactoryTest {

	private WorkItemListFactory factory;

	@BeforeMethod
	public void setUp() throws Exception {
		factory = new WorkItemListFactory();
	}

	@Test
	public void shouldProduceEmptyList() throws Exception {
		List<WorkItem> list = factory.produceWorkItems();

		Assert.assertNotNull(list);
		Assert.assertEquals(list.size(), 0);
	}
}
