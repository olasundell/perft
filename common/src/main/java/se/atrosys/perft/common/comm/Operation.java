package se.atrosys.perft.common.comm;

import java.io.Serializable;

public enum Operation implements Serializable {
	NOOP,
	REGISTER,
	GET_WORK,
	START_WORK,
	SEND_RESULTS,
	RESULTS_RECEIVED
}
