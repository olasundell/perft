package se.atrosys.perft.hub;

import org.testng.Assert;
import org.testng.annotations.Test;
import se.atrosys.perft.hub.workproduction.AccessLogConverterImpl;

import java.io.IOException;
import java.util.List;

public class AccessLogConverterTest {
	@Test
	public void shouldConvertMinorAccessLog() throws IOException {
		List<AccessLogConverterImpl.AccessLogItem> list = new AccessLogConverterImpl().parseLog("accesslog");

		Assert.assertNotNull(list);
		Assert.assertNotEquals(list.size(), 0);
	}
}
