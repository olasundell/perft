package se.atrosys.perft.server;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class AccessLogConverterTest {
	@Test
	public void shouldConvertMinorAccessLog() throws IOException {
		List<AccessLogConverter.AccessLogItem> list = new AccessLogConverter().parseLog("accesslog");

		Assert.assertNotNull(list);
		Assert.assertNotEquals(list.size(), 0);
	}
}
