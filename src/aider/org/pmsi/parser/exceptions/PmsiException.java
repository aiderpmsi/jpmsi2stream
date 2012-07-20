package aider.org.pmsi.parser.exceptions;

public class PmsiException extends Exception {

	private String xmlMessage = "";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -336347335654324056L;

	public PmsiException() {
		// TODO Auto-generated constructor stub
	}

	public PmsiException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public PmsiException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public PmsiException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public String getXmlMessage() {
		return xmlMessage;
	}

	public PmsiException setXmlMessage(String xmlMessage) {
		this.xmlMessage = xmlMessage;
		return this;
	}

}
