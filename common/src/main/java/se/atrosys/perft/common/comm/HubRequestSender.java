package se.atrosys.perft.common.comm;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.atrosys.perft.common.NodeToHubRequest;

public abstract class HubRequestSender {
	protected final EventLoopGroup workerGroup;
	protected final String host;
	protected final int port;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public HubRequestSender(int port, String host) {
		workerGroup = new NioEventLoopGroup();
		this.port = port;
		this.host = host;
	}

	protected void sendToHub(NodeToHubRequest msg) {
		try {
			Bootstrap bootstrap = createBootstrap(workerGroup);

			ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
			channelFuture.channel().pipeline().writeAndFlush(msg) ;

			// Wait until the connection is closed.
			logger.info("Closing connection");
			channelFuture.channel().closeFuture().sync();
			logger.info("Connection closed");
		} catch (InterruptedException e) {
			logger.warn("Interrupted!", e);
		} finally {
			workerGroup.shutdownGracefully();
		}
	}

	private Bootstrap createBootstrap(EventLoopGroup workerGroup) {
		Bootstrap b = new Bootstrap(); // (1)
		b.group(workerGroup); // (2)
		b.channel(NioSocketChannel.class); // (3)
		b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
		b.handler(createChannelInitializer());
		return b;
	}

	protected abstract ChannelInitializer<SocketChannel> createChannelInitializer();
}
