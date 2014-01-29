package se.atrosys.perft.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {
	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger(Client.class);

//		String host = args[0];
//		int port = Integer.parseInt(args[1]);

		String host = "localhost";
		int port = 7800;

		new WorkerClient(host, port);
	}
}
