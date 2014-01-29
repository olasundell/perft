package se.atrosys.perft.server;

import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.atrosys.perft.common.Operation;
import se.atrosys.perft.common.WorkerConfig;

import java.util.List;

public class WorkerServerHandler extends ChannelInboundHandlerAdapter {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private final WorkerConfig workerConfig;

	public WorkerServerHandler(WorkerConfig workerConfig) {
		this.workerConfig = workerConfig;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

	}

	@Override
	public void channelActive(final ChannelHandlerContext context) { // (1)
		logger.info("Channel active!");

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
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.error("Error!", cause);
		ctx.close();
	}
}
