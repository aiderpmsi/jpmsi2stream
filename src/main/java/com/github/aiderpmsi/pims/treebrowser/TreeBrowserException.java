package com.github.aiderpmsi.pims.treebrowser;

public class TreeBrowserException extends Exception {

	/** Id */
	private static final long serialVersionUID = -7285178235758606502L;

	public TreeBrowserException() {
		super();
	}

	public TreeBrowserException(String message) {
		super(message);
	}

	public TreeBrowserException(Throwable cause) {
		super(cause);
	}

	public TreeBrowserException(String message, Throwable cause) {
		super(message, cause);
	}

	public TreeBrowserException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
