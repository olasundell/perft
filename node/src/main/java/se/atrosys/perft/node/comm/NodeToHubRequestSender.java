package se.atrosys.perft.node.comm;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.atrosys.perft.common.comm.NodeToHubRequest;
import se.atrosys.perft.common.comm.Operation;
import se.atrosys.perft.common.work.ResultItem;
import se.atrosys.perft.common.comm.HubRequestSender;
import se.atrosys.perft.node.NodeMain;

import java.util.List;

public class NodeToHubRequestSender extends HubRequestSender {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	public NodeToHubRequestSender(String host, int port) {
		super(port, host);
	}

	public void getWork() {
		sendToHub(new NodeToHubRequest(Operation.GET_WORK, NodeMain.getNodeInfo()));
	}

	public void sendResults(List<ResultItem> resultItems) {
		logger.info("Sending results...");
		sendToHub(new NodeToHubRequest(Operation.SEND_RESULTS, NodeMain.getNodeInfo()).addResults(resultItems));
		logger.info("Results sent.");
	}

	public void register() {
		logger.info("Sending register request...");
		sendToHub(new NodeToHubRequest(Operation.REGISTER, NodeMain.getNodeInfo()));
	}

	@Override
	protected ChannelInitializer<SocketChannel> createChannelInitializer() {
		return new ChannelInitializer<SocketChannel>() { // (4)
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline()
						.addLast(new ObjectEncoder())
						.addLast(new ObjectDecoder(ClassResolvers.softCachingResolver(ClassLoader.getSystemClassLoader())))
						.addLast(new HubRequestHandler())
						.addLast(new WorkerConfigHandler())
						.addLast(new ObjectHandler());
			}
		};
	}

	public void close() {

	}
}
