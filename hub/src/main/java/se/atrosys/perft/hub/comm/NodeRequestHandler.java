package se.atrosys.perft.hub.comm;

import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.atrosys.perft.common.HubToNodeRequest;
import se.atrosys.perft.common.NodeToHubRequest;
import se.atrosys.perft.common.Operation;
import se.atrosys.perft.common.WorkerConfig;
import se.atrosys.perft.hub.Main;

public class NodeRequestHandler extends SimpleChannelInboundHandler<NodeToHubRequest> {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private final WorkerConfig workerConfig;

	public NodeRequestHandler(WorkerConfig workerConfig) {
		this.workerConfig = workerConfig;
	}

	@Override
	protected void channelRead0(final ChannelHandlerContext context, NodeToHubRequest request) throws Exception {
		logger.info("Channel read, with operation " + request);

		logger.info("Acting on operation");
		switch (request.getOperation()) {
			case GET_WORK:
				sendWorkToClient(context);
				break;
			case REGISTER:
				registerClient(context);
			default:
				logger.error("Unknown error with operation {}", request.getOperation());
		}
	}

	private synchronized void registerClient(final ChannelHandlerContext context) {
		int nextId = Main.getNextId();
		Main.clients.put(nextId, context.channel());
		final ChannelFuture channelFuture = context.writeAndFlush(new HubToNodeRequest(Operation.REGISTER, nextId));

		channelFuture.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) {
				assert channelFuture == future;
				context.close();
			}
		});
	}

	private void sendWorkToClient(final ChannelHandlerContext context) {
		final ChannelFuture channelFuture = context.writeAndFlush(workerConfig);

		channelFuture.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) {
				assert channelFuture == future;
				context.close();
			}
		});
	}

	@Override
	public void channelActive(final ChannelHandlerContext context) {
		logger.info("Channel active!");

		context.read();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.error("Error!", cause);
		ctx.close();
	}
}
