package se.atrosys.perft.node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.atrosys.perft.common.NodeInfo;
import se.atrosys.perft.node.comm.NodeListener;
import se.atrosys.perft.node.comm.NodeToHubRequestSender;

public class NodeMain {
	public static final int INET_PORT = 7801;
	private static int id = 0;
	private static NodeInfo nodeInfo = new NodeInfo(INET_PORT);
	public static boolean finished = false;

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private final NodeListener nodeListener;
	private final NodeToHubRequestSender hubRequestSender;

	private NodeMain() {

		String host = "localhost";
		int port = 7800;

		hubRequestSender = new NodeToHubRequestSender(host, port);

		hubRequestSender.register();
		nodeListener = new NodeListener();
	}

	public static void main(String[] args) {
		new NodeMain();
	}

	public void shutdown() {
		nodeListener.close();
		hubRequestSender.close();
		System.exit(0);
	}

	public static NodeInfo getNodeInfo() {
		return nodeInfo;
	}

	public static void setId(int id) {
		NodeMain.nodeInfo.setId(id);
	}

}
