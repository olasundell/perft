package se.atrosys.perft.common.comm;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HubRequestSender {
	protected final EventLoopGroup workerGroup;
	protected final String host;
	protected final int port;
	private Logger logger = LoggerFactory.getLogger(HubRequestSender.class);
	private ChannelFuture channelFuture;
	private boolean finished = false;
	private final Bootstrap bootstrap;

	public HubRequestSender(int port, String host) {
		this.port = port;
		this.host = host;

		workerGroup = new NioEventLoopGroup();
		bootstrap = createBootstrap(workerGroup);

		refreshChannelFuture();
	}

	private void refreshChannelFuture() {
		logger.info("Refreshing channel future.");
		try {
			channelFuture = bootstrap.connect(host, port).sync();
		} catch (InterruptedException e) {
			logger.warn("Interrupted!", e);
		}
	}

	public void exit() {
		// Wait until the connection is closed.
		logger.info("Closing connection");

		try {
			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			logger.warn("Interrupted!", e);
		} finally {
			workerGroup.shutdownGracefully();
		}

		logger.info("Connection closed");
	}

	protected void sendToHub(Request msg) {
		if (channelFuture.isCancelled()) {
			refreshChannelFuture();
		}

		channelFuture.channel().pipeline().writeAndFlush(msg);
	}

	private Bootstrap createBootstrap(EventLoopGroup workerGroup) {
		Bootstrap b = new Bootstrap();
		b.group(workerGroup);
		b.channel(NioSocketChannel.class);
		b.option(ChannelOption.SO_KEEPALIVE, true);
		b.handler(createChannelInitializer());
		return b;
	}

	protected abstract ChannelInitializer<SocketChannel> createChannelInitializer();
}
