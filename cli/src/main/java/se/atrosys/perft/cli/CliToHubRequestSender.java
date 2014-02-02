package se.atrosys.perft.cli;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.atrosys.perft.common.comm.CliToHubRequest;
import se.atrosys.perft.common.comm.Operation;
import se.atrosys.perft.common.comm.HubRequestSender;

public class CliToHubRequestSender extends HubRequestSender {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public CliToHubRequestSender(String host, int port) {
		super(port, host);
	}

	public void startWork(String filename) {
		logger.info("Sending access log filename to hub, which will start work");
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
