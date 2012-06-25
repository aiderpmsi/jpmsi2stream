package aider.org.pmsi.dto;

import aider.org.pmsi.parser.linestypes.PmsiLineType;

import com.sleepycat.dbxml.XmlException;

public interface DTOPmsiLineType {

	public void start(String name) throws XmlException;
	
	public void appendContent(PmsiLineType lineType) throws XmlException;
	
	public void end() throws XmlException;
	
	public void close() throws XmlException;	
}
