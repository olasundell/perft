package se.atrosys.perft.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.atrosys.perft.common.ResultItem;

import java.util.List;

public class ResultSummarizer {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public void summarize(List<ResultItem> resultItems) {
		int noOfOK = 0;
		int totalTimeSpent = 0;
		long first=Long.MAX_VALUE;
		long last=-1;



		for (ResultItem resultItem : resultItems) {
			if (resultItem.getStartTime() < first) {
				first = resultItem.getStartTime();
			}

			if (resultItem.getEndTime() > last) {
				last = resultItem.getEndTime();
			}

			if (resultItem.getStatusCode() == 200) {
				noOfOK++;
			}

			totalTimeSpent += resultItem.getTime();
		}

		logger.info(String.format("Average time: %d ms", totalTimeSpent / resultItems.size()));
		logger.info(String.format("No of requests: %d No of OK requests: %d", resultItems.size(), noOfOK));
		logger.info(String.format("No of requests per second: %d", (resultItems.size() * 1000) / (last - first)));
	}
}
