package se.atrosys.perft.common.comm;

import se.atrosys.perft.common.work.ResultItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NodeToHubRequest extends Request implements Serializable {
	private final NodeInfo nodeInfo;
	private List<ResultItem> results = new ArrayList<ResultItem>();

	public NodeToHubRequest(Operation operation, NodeInfo nodeInfo) {
		super(operation);
		this.nodeInfo = nodeInfo;
	}

	public NodeInfo getNodeInfo() {
		return nodeInfo;
	}

	public Request addResults(List<ResultItem> resultItems) {
		results.addAll(resultItems);

		return this;
	}

	public List<ResultItem> getResults() {
		return results;
	}
}
