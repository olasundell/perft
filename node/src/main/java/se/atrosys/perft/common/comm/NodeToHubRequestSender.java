package se.atrosys.perft.common.comm;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import se.atrosys.perft.common.NodeToHubRequest;
import se.atrosys.perft.common.Operation;
import se.atrosys.perft.common.ResultItem;

import java.util.List;

public class NodeToHubRequestSender extends HubRequestSender {

	public NodeToHubRequestSender(String host, int port) {
		super(port, host);
	}

	public void getWork() {
		sendToHub(new NodeToHubRequest(Operation.GET_WORK, Node.getId()));
	}

	public void sendResults(List<ResultItem> resultItems) {
		sendToHub(new NodeToHubRequest(Operation.SEND_RESULTS, Node.getId()));
	}

	public void register() {
		sendToHub(new NodeToHubRequest(Operation.REGISTER, -1));
	}

	@Override
	protected ChannelInitializer<SocketChannel> createChannelInitializer() {
		return new ChannelInitializer<SocketChannel>() { // (4)
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline()
						.addLast(new ObjectEncoder())
						.addLast(new ObjectDecoder(ClassResolvers.softCachingResolver(ClassLoader.getSystemClassLoader())))
						.addLast(new WorkerConfigHandler());
			}
		};
	}
}
