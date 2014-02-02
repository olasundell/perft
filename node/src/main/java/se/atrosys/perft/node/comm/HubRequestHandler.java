package se.atrosys.perft.node.comm;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.atrosys.perft.common.comm.HubToNodeRequest;
import se.atrosys.perft.common.comm.Operation;
import se.atrosys.perft.node.NodeMain;

public class HubRequestHandler extends SimpleChannelInboundHandler<HubToNodeRequest> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.error("Error!", cause);
		ctx.close();
	}

	protected void channelRead0(ChannelHandlerContext ctx, HubToNodeRequest msg) throws Exception {
		logger.info("Channel read");
		if (Operation.REGISTER.equals(msg.getOperation())) {
			logger.info("Got registry information, id {}", msg.getClientId());
			NodeMain.setId(msg.getClientId());
		}
	}
}
