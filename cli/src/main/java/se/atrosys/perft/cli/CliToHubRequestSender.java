package se.atrosys.perft.cli;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import se.atrosys.perft.common.CliToHubRequest;
import se.atrosys.perft.common.Operation;
import se.atrosys.perft.node.comm.HubRequestSender;

public class CliToHubRequestSender extends HubRequestSender {
	public CliToHubRequestSender(String host, int port) {
		super(port, host);
	}

	public void startWork(String filename) {
		sendToHub(new CliToHubRequest(Operation.START_WORK, filename));
	}

	@Override
	protected ChannelInitializer<SocketChannel> createChannelInitializer() {
		return new ChannelInitializer<SocketChannel>() { // (4)
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline()
						.addLast(new ObjectEncoder())
						.addLast(new ObjectDecoder(ClassResolvers.softCachingResolver(ClassLoader.getSystemClassLoader())))
						.addLast(new CliRequestHandler());
			}
		};
	}
}
