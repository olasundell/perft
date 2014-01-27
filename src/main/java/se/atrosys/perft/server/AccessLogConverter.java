package se.atrosys.perft.server;

import java.io.IOException;
import java.util.List;

public interface AccessLogConverter {
	List<AccessLogItem> parseLog(String fileName) throws IOException;

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
