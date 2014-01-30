package se.atrosys.perft.common;

import java.io.Serializable;

public enum Operation implements Serializable {
//	public static int NOOP = -1;
//	public static int GET_WORK = 0;
//	public static int SEND_RESULTS = 1;
	NOOP,
	REGISTER,
	GET_WORK,
	SEND_RESULTS
}
