package aider.org.pmsi.dto;

import aider.org.pmsi.parser.linestypes.PmsiLineType;

public interface DtoPmsiLineType {

	public void writeStartDocument(String name) throws DtoPmsiException;
	
	public void writeStartElement(String name) throws DtoPmsiException;
	
	public void writeEndElement() throws DtoPmsiException;

	public void writeLineElement(PmsiLineType lineType)  throws DtoPmsiException;
	
	public void writeEndDocument() throws DtoPmsiException;
	
	public void close() throws DtoPmsiException;
	
	public PmsiLineType getLastLine() throws DtoPmsiException;
}
