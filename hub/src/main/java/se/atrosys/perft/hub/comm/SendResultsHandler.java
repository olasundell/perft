package se.atrosys.perft.hub.comm;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.atrosys.perft.common.ResultItem;
import se.atrosys.perft.hub.HubMain;

import java.util.List;

public class SendResultsHandler extends SimpleChannelInboundHandler<List<ResultItem>> {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	protected void channelRead0(ChannelHandlerContext ctx, List<ResultItem> resultItems) throws Exception {
		logger.info("Channel read, getting resultItems");
		logger.info("Results size {}", resultItems.size());
		HubMain.results.put(0, resultItems);
		HubMain.finished = true;
		ctx.channel().parent().close();
	}
}
