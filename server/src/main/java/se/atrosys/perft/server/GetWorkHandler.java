package se.atrosys.perft.server;

import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.atrosys.perft.common.ClientToServerRequest;
import se.atrosys.perft.common.Operation;
import se.atrosys.perft.common.WorkerConfig;

public class GetWorkHandler extends SimpleChannelInboundHandler<ClientToServerRequest> {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private final WorkerConfig workerConfig;

	public GetWorkHandler(WorkerConfig workerConfig) {
		this.workerConfig = workerConfig;
	}

	@Override
	protected void channelRead0(final ChannelHandlerContext context, ClientToServerRequest request) throws Exception {
		logger.info("Channel read, with operation " + request);

		logger.info("Acting on operation");
		switch (request.getOperation()) {
			case GET_WORK:
				sendWorkToClient(context);
				break;
			default:
				logger.error("Unknown error with operation {}", request.getOperation());
		}
	}

	private void sendWorkToClient(final ChannelHandlerContext context) {
		final ChannelFuture channelFuture = context.writeAndFlush(workerConfig); // (3)

		channelFuture.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) {
				assert channelFuture == future;
				context.close();
			}
		}); // (4)
	}

	@Override
	public void channelActive(final ChannelHandlerContext context) { // (1)
		logger.info("Channel active!");

		context.read();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.error("Error!", cause);
		ctx.close();
	}
}
