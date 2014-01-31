package se.atrosys.perft.node;

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
import se.atrosys.perft.common.NodeInfo;
import se.atrosys.perft.node.comm.NodeToHubRequestSender;
import se.atrosys.perft.node.comm.WorkerConfigHandler;

public class NodeMain {
	public static final int INET_PORT = 7801;
	private static int id = 0;
	private static NodeInfo nodeInfo = new NodeInfo(INET_PORT);
	public static boolean finished = false;

	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger(NodeMain.class);

		String host = "localhost";
		int port = 7800;

		NodeToHubRequestSender hubRequestSender = new NodeToHubRequestSender(host, port);
		hubRequestSender.register();
		NodeListener nodeLister = new NodeListener();
	}

	public static NodeInfo getNodeInfo() {
		return nodeInfo;
	}

	public static void setId(int id) {
		NodeMain.nodeInfo.setId(id);
	}

	private static class NodeListener {
		private final Logger logger = LoggerFactory.getLogger(this.getClass());

		NodeListener() {
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
										.addLast(new WorkerConfigHandler());
							}
						})
						.option(ChannelOption.SO_BACKLOG, 128)          // (5)
						.childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

				// Bind and start to accept incoming connections.
				ChannelFuture f = b.bind(INET_PORT).sync(); // (7)

				// Wait until the server socket is closed.
				// In this example, this does not happen, but you can do that to gracefully
				// shut down your server.
				logger.info("NodeListener started!");
				f.channel().closeFuture().sync();

			} catch (InterruptedException e) {
				logger.warn("Interrupted!", e);
			} finally {
				logger.info("Shutting down.");
				workerGroup.shutdownGracefully();
				bossGroup.shutdownGracefully();
			}
		}
	}
}
