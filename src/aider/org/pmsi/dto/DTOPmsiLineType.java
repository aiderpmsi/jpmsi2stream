package aider.org.pmsi.dto;

import java.io.FileNotFoundException;
import java.util.Stack;

import aider.org.pmsi.parser.linestypes.PmsiLineType;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009Header;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009a;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009b;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009c;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009h;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009m;

import com.sleepycat.dbxml.XmlContainer;
import com.sleepycat.dbxml.XmlDocument;
import com.sleepycat.dbxml.XmlDocumentConfig;
import com.sleepycat.dbxml.XmlEventWriter;
import com.sleepycat.dbxml.XmlException;
import com.sleepycat.dbxml.XmlManager;
import com.sleepycat.dbxml.XmlQueryContext;
import com.sleepycat.dbxml.XmlQueryExpression;
import com.sleepycat.dbxml.XmlResults;
import com.sleepycat.dbxml.XmlValue;

public class DTOPmsiLineType {

	private XmlManager xmlManager = null;
	
	private XmlContainer xmlContainer = null;
	
	private XmlDocument document = null;
	
	private XmlEventWriter writer = null;
	
	private Stack<PmsiLineType> lastLine = new Stack<PmsiLineType>();
	
	private String name = null;
	
	public DTOPmsiLineType() throws XmlException, FileNotFoundException {
		xmlManager = new XmlManager();
		xmlManager.setDefaultContainerType(XmlContainer.NodeContainer);
		
		xmlContainer = xmlManager.openContainer("catalog.dbxml");
	}
	
	public void start(String name) throws XmlException {
		this.name = name;
		if (document != null)
			throw new RuntimeException("Document already initialized");
		
		document = xmlManager.createDocument();
        
		XmlDocumentConfig config = new XmlDocumentConfig();
		config.setGenerateName(true);
		
        writer = xmlContainer.putDocumentAsEventWriter(document, config);
            
        writer.writeStartDocument(null, null, null);
        
        // Ouverture du xml
        writer.writeStartElement(name, null, null, 0, false);
        writer.writeStartElement("content", null, null, 0, false);
	}
	
	public void appendContent(PmsiLineType lineType) throws XmlException {
		if (document == null)
			throw new RuntimeException("Document not initialized");
		
		if (lineType instanceof PmsiRsf2009Header) {
			// Ecriture de la ligne header sans la fermer (va contenir les rsf)
			writeSimpleElement(lineType);
			// Prise en compte de l'ouverture de la ligne
			lastLine.add(lineType);
		} else if (lineType instanceof PmsiRsf2009a) {
			// Si un rsfa est ouvert, il faut le fermer
			if (lastLine.lastElement() instanceof PmsiRsf2009a) {
				writer.writeEndElement(lastLine.pop().getName(), null, null);
			}
			// ouverture du rsfa
			writeSimpleElement(lineType);
			// Prise en compte de l'ouverture de ligne
			lastLine.add(lineType);
		} else if (lineType instanceof PmsiRsf2009b || lineType instanceof PmsiRsf2009c ||
				lineType instanceof PmsiRsf2009h || lineType instanceof PmsiRsf2009m) {
			// Ouverture de la ligne
			writeSimpleElement(lineType);
			// fermeture de la ligne
			writer.writeEndElement(lineType.getName(), null, null);
		}
	}
	
	public void end() throws XmlException {
		if (document == null)
			throw new RuntimeException("Document not initialized");

		while (!lastLine.empty()) {
			writer.writeEndElement(lastLine.pop().getName(), null, null);
		}
		writer.writeEndElement("content", null, null);
		writer.writeEndElement(name, null, null);
		
        // End the document
        writer.writeEndDocument();
        // Close the document
        writer.close();
        
        writer = null;
        document = null;
	}
	
	public void printContent() throws XmlException {
        XmlQueryContext context = xmlManager.createQueryContext();
        context.setEvaluationType(XmlQueryContext.Lazy); 
        String myQuery = 
                    "collection('catalog.dbxml')//*";
        XmlQueryExpression qe = xmlManager.prepare(myQuery, context);
            
        XmlResults results = qe.execute(context);
            
        while (results.hasNext()) {
        	XmlValue xmlValue = results.next();
        	System.out.println(xmlValue.asString());
        }
            
        results.delete();
        qe.delete();
        
        throw new RuntimeException("Stop");
	}
	
	public void close() throws XmlException {
		xmlContainer.close();
		xmlManager.close();
	}
	
	private void writeSimpleElement(PmsiLineType lineType) throws XmlException {
		writeSimpleElement(lineType.getName(), lineType.getNames(), lineType.getContent());
	}
	
	private void writeSimpleElement(String name, String[] attNames, String[] attContent) throws XmlException {
		writer.writeStartElement(name, null, null, attNames.length, false);
		for (int i = 0 ; i < attNames.length ; i++) {
			writer.writeAttribute(attNames[i], null, null, attContent[i], false);
		}
	}
	
}
