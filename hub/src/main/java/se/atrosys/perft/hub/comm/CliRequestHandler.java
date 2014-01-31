package se.atrosys.perft.hub.comm;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.atrosys.perft.common.CliToHubRequest;

public class CliRequestHandler extends SimpleChannelInboundHandler<CliToHubRequest> {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, CliToHubRequest msg) throws Exception {
		logger.info("Channel open! Filename received: {}", msg.getFilename());
	}
}
