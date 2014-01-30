package se.atrosys.perft.common.comm;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import se.atrosys.perft.common.HubToNodeRequest;
import se.atrosys.perft.common.Operation;

public class HubRequestHandler extends SimpleChannelInboundHandler<HubToNodeRequest> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HubToNodeRequest msg) throws Exception {
		if (Operation.REGISTER.equals(msg.getOperation())) {
			int id = msg.getClientId();
		}
	}
}
