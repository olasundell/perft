package se.atrosys.perft.hub.comm;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.atrosys.perft.common.NodeInfo;
import se.atrosys.perft.common.WorkerConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HubServer {
	public static ConcurrentHashMap<NodeInfo, ChannelHandlerContext> clients = new ConcurrentHashMap<NodeInfo, ChannelHandlerContext>();
	private int port;
	private WorkerConfig workerConfig;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public HubServer(int port) {
		this.port = port;
	}

	public void run() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
		EventLoopGroup workerGroup = new NioEventLoopGroup();
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
									.addLast(new CliRequestHandler(HubServer.this))
									.addLast(new NodeRequestHandler(workerConfig));
						}
					})
					.option(ChannelOption.SO_BACKLOG, 128)          // (5)
					.childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

			// Bind and start to accept incoming connections.
			ChannelFuture f = b.bind(port).sync(); // (7)

			// Wait until the server socket is closed.
			// In this example, this does not happen, but you can do that to gracefully
			// shut down your server.
			logger.info("Server started!");
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public void sendWork() {
		logger.info("Sending work to all clients, {} in total", clients.size());

		for (Map.Entry<NodeInfo, ChannelHandlerContext> entry: clients.entrySet()) {
			logger.info("Sending work to client {}", entry.getKey().getId());

			ChannelHandlerContext context = entry.getValue();
			context.pipeline().writeAndFlush(workerConfig);

			logger.info("Work sent.");
		}
	}

	public HubServer withWorkerConfig(WorkerConfig workerConfig) {
		this.workerConfig = workerConfig;
		return this;
	}
}
