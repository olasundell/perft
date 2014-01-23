package se.atrosys.perft.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.atrosys.perft.common.Result;

import java.util.List;

public class ResultSummarizer {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public void summarize(List<Result> results) {
		int noOfOK = 0;
		int totalTimeSpent = 0;

		for (Result result: results) {
			if (result.getStatusCode() == 200) {
				noOfOK++;
			}

			totalTimeSpent += result.getTime();
		}

		logger.info(String.format("Average time: %d ms", totalTimeSpent / results.size()));
	}
}
