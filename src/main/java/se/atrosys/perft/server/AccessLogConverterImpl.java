package se.atrosys.perft.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccessLogConverterImpl implements AccessLogConverter {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
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

}
