package aider.org.pmsi.dto;

import javax.xml.xquery.XQException;

import aider.org.pmsi.parser.linestypes.PmsiLineType;

public interface DTOPmsiLineType {

	public void start(String name);
	
	public void appendContent(PmsiLineType lineType);
	
	public void end() throws InterruptedException, XQException;
	
	public void close() throws XQException, InterruptedException;
}
