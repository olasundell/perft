package se.atrosys.perft.common;

public class Result {
	private int statusCode;
	private long endTime;
	private long startTime;
	private boolean failed;

	public long getTime() {
		return endTime - startTime;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public void markAsFailed() {
		this.failed = true;
	}
	
	public boolean isFailed() {
		return failed;
	}
}
