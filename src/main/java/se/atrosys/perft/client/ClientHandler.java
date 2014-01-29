package se.atrosys.perft.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.atrosys.perft.common.WorkItem;
import se.atrosys.perft.common.WorkerConfig;

import java.util.Date;
import java.util.List;

public class ClientHandler extends SimpleChannelInboundHandler<WorkerConfig> {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.error("Error!", cause);
		ctx.close();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, WorkerConfig workerConfig) throws Exception {
		logger.info("Channel read.");
		List<WorkItem> workItems = workerConfig.getWorkItems();

		if (workItems != null) {
			logger.info("Received list of size {}", workItems.size());
			if (workItems.size() > 0) {
				logger.info("First item's URI is {}", workItems.get(0).getUri());
			}
		}


		new WorkerSpawner(workerConfig).workOnItems();
	}
}
