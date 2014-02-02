package se.atrosys.perft.hub.comm;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.atrosys.perft.common.comm.CliToHubRequest;

public class CliRequestHandler extends SimpleChannelInboundHandler<CliToHubRequest> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final HubServer hubServer;

	public CliRequestHandler(HubServer hubServer) {
		this.hubServer = hubServer;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, CliToHubRequest msg) throws Exception {
		logger.info("Channel open! Filename received: {}", msg.getFilename());
		hubServer.sendWork();
	}
}
