package se.atrosys.perft.client;

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

public class WorkerClient {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	public WorkerClient(String host, int port) {
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			Bootstrap bootstrap = createBootstrap(workerGroup);

			// Start the client.
			ChannelFuture channelFuture = bootstrap.connect(host, port).sync(); // (5)

			// Wait until the connection is closed.
			channelFuture.channel().closeFuture().sync();
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

	private ChannelInitializer<SocketChannel> createChannelInitializer() {
		return new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();

				ClassResolver classResolver = ClassResolvers.softCachingResolver(ClassLoader.getSystemClassLoader());

				pipeline.addLast(new ObjectDecoder(1718579048, classResolver));
				pipeline.addLast(new ObjectEncoder());
				pipeline.addLast(new ClientHandler());
			}
		};
	}
}
