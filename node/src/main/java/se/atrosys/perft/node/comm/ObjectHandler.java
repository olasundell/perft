package se.atrosys.perft.node.comm;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectHandler extends SimpleChannelInboundHandler<Object> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.info("Channel read");
	}
}
