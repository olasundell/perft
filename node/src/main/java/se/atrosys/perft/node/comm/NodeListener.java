package se.atrosys.perft.node.comm;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.atrosys.perft.node.NodeMain;

public class NodeListener {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final EventLoopGroup workerGroup;
	private final EventLoopGroup bossGroup;

	public NodeListener() {
		bossGroup = new NioEventLoopGroup();
		workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap(); // (2)
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class) // (3)
					.childHandler(new ChannelInitializer<SocketChannel>() { // (4)
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline()
									.addLast(new ObjectEncoder())
									.addLast(new ObjectDecoder(ClassResolvers.softCachingResolver(ClassLoader.getSystemClassLoader())))
									.addLast(new WorkerConfigHandler());
						}
					})
					.option(ChannelOption.SO_BACKLOG, 128)          // (5)
					.childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

			ChannelFuture f = b.bind(NodeMain.INET_PORT).sync(); // (7)

			logger.info("NodeListener started!");
			f.channel().closeFuture().sync();

		} catch (InterruptedException e) {
			logger.warn("Interrupted!", e);
		}
	}

	public void close() {
		logger.info("Shutting down.");
		workerGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();
	}
}
