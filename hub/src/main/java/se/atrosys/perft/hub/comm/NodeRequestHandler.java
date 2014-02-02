package se.atrosys.perft.hub.comm;

import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.atrosys.perft.common.comm.HubToNodeRequest;
import se.atrosys.perft.common.comm.NodeInfo;
import se.atrosys.perft.common.comm.NodeToHubRequest;
import se.atrosys.perft.common.comm.Operation;
import se.atrosys.perft.common.work.config.WorkerConfig;
import se.atrosys.perft.hub.HubMain;

public class NodeRequestHandler extends SimpleChannelInboundHandler<NodeToHubRequest> {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private final WorkerConfig workerConfig;

	public NodeRequestHandler(WorkerConfig workerConfig) {
		this.workerConfig = workerConfig;
	}

	@Override
	protected void channelRead0(final ChannelHandlerContext context, NodeToHubRequest request) throws Exception {
		logger.info("Channel read, with operation " + request.getOperation());

		logger.info("Acting on operation");
		switch (request.getOperation()) {
			case GET_WORK:
				sendWorkToClient(context);
				break;
			case REGISTER:
				registerClient(request.getNodeInfo(), context);
				break;
			case SEND_RESULTS:
				if (receiveResults(request)) {
					context.channel().parent().close();
				}

				break;
			default:
				logger.error("Unknown operation {}", request.getOperation());
		}
	}

	private boolean receiveResults(NodeToHubRequest request) {
		logger.info("Results size {}", request.getResults().size());
		return HubMain.putResults(request.getNodeInfo(), request.getResults());
	}

	private synchronized void registerClient(final NodeInfo nodeInfo, ChannelHandlerContext context) {
		int nextId = HubMain.getNextId();

		logger.info("Registering client with id {}", nextId);

		nodeInfo.setId(nextId);
		HubServer.clients.put(nodeInfo, context);

		context.writeAndFlush(new HubToNodeRequest(Operation.REGISTER, nextId));

	}

	private void sendWorkToClient(final ChannelHandlerContext context) {
		context.pipeline().writeAndFlush(workerConfig);
	}

	@Override
	public void channelActive(final ChannelHandlerContext context) {
		try {
			super.channelActive(context);
		} catch (Exception e) {
			logger.error("Error occurred!", e);
		}

		logger.info("Channel active!");

		context.read();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.error("Error!", cause);
		ctx.close();
	}
}
