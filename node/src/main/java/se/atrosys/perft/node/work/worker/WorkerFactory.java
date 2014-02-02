package se.atrosys.perft.node.work.worker;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public interface WorkerFactory {
	Worker createWorker(PoolingHttpClientConnectionManager connectionManager);
}
