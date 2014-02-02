package se.atrosys.perft.common.comm;

import java.io.Serializable;

public class NodeInfo implements Serializable {
	private int inetPort;
	private int id;

	public NodeInfo(int inetPort) {
		this.inetPort = inetPort;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
