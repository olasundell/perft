package se.atrosys.perft.hub.comm;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.atrosys.perft.common.WorkItem;

import java.util.Collections;
import java.util.List;

public class HubServerTest {
	private HubServer server;

	@BeforeMethod
	public void setup() {
		server = new HubServer(0);
	}

	@Test
	public void shouldReturnEmptyLists() {
		int numberOfClients = 3;

		List<WorkItem> empty = Collections.emptyList();
		List<List<WorkItem>> list = server.splitWorkList(empty, numberOfClients);

		Assert.assertNotNull(list);
		Assert.assertEquals(list.size(), numberOfClients);

		for (int i=0 ; i<numberOfClients ; i++) {
			Assert.assertNotNull(list.get(i));
			Assert.assertEquals(list.get(i).size(), 0);
		}
	}

	@Test
	public void shouldReturnCorrectlySizedListsWhenUnevenNumber() {
		int numberOfClients = 3;

		List<List<WorkItem>> list = server.splitWorkList(Collections.nCopies(2, new WorkItem(null)), numberOfClients);

		Assert.assertEquals(list.get(0).size(), 1);
		Assert.assertEquals(list.get(1).size(), 1);
		Assert.assertEquals(list.get(2).size(), 0);
	}

	@Test
	public void shouldReturnCorrectlySizedListsWhenEvenNumber() {
		int numberOfClients = 4;

		List<List<WorkItem>> list = server.splitWorkList(Collections.nCopies(8, new WorkItem(null)), numberOfClients);

		Assert.assertEquals(list.get(0).size(), 2);
		Assert.assertEquals(list.get(1).size(), 2);
		Assert.assertEquals(list.get(2).size(), 2);
		Assert.assertEquals(list.get(3).size(), 2);
	}
}
