package com.github.aiderpmsi.pims.treemodel;

public class NodeException extends Exception {

	/** Serial */
	private static final long serialVersionUID = -3141136726841717748L;

	public NodeException() {
	}

	public NodeException(String message) {
		super(message);
	}

	public NodeException(Throwable cause) {
		super(cause);
	}

	public NodeException(String message, Throwable cause) {
		super(message, cause);
	}

	public NodeException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
