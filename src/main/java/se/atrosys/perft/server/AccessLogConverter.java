package se.atrosys.perft.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccessLogConverter {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	public List<AccessLogItem> parseLog(String fileName) throws IOException {
		ArrayList<AccessLogItem> accessLogItems = new ArrayList<AccessLogItem>();

		String logEntryPattern = "^([\\d.]+) - - \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"([A-Z]+) (.+?) (.+?)\" (\\d+) .*";

		Pattern p = Pattern.compile(logEntryPattern);

		File file = new File(fileName);

		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		String logEntryLine;

		while ((logEntryLine = bufferedReader.readLine()) != null) {
			Matcher matcher = p.matcher(logEntryLine);

			if (matcher.matches()) {

				accessLogItems.add(new AccessLogItem(matcher.group(5), matcher.group(4), matcher.group(6)));

				logger.trace(String.format("Datetime: %s Request: %s Status code: %s",
						matcher.group(2),
						matcher.group(4),
						matcher.group(6)));
			} else {
				logger.info(String.format("Could not find match for row %s", logEntryLine));
			}
		}

		return accessLogItems;
	}

	public static class AccessLogItem {
		private int statusCode;
		private String method;
		private String request;

		public AccessLogItem(String method, String request, String statusCode) {
			this.statusCode = Integer.parseInt(statusCode);
			this.method = method;
			this.request = request;
		}

		public int getStatusCode() {
			return statusCode;
		}

		public void setStatusCode(int statusCode) {
			this.statusCode = statusCode;
		}

		public String getMethod() {
			return method;
		}

		public void setMethod(String method) {
			this.method = method;
		}

		public String getRequest() {
			return request;
		}

		public void setRequest(String request) {
			this.request = request;
		}
	}
}
