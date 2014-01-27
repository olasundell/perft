package se.atrosys.perft.server;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.atrosys.perft.common.Result;

import java.util.List;

public class ResultSummarizer {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public void summarize(List<Result> results) {
		int noOfOK = 0;
		int totalTimeSpent = 0;
		long first=Long.MAX_VALUE;
		long last=-1;



		for (Result result: results) {
			if (result.getStartTime() < first) {
				first = result.getStartTime();
			}

			if (result.getEndTime() > last) {
				last = result.getEndTime();
			}

			if (result.getStatusCode() == 200) {
				noOfOK++;
			}

			totalTimeSpent += result.getTime();
		}

		logger.info(String.format("Average time: %d ms", totalTimeSpent / results.size()));
		logger.info(String.format("No of requests: %d No of OK requests: %d", results.size(), noOfOK));
		logger.info(String.format("No of requests per second: %d", (results.size() * 1000) / (last - first)));
	}
}
