package com.github.aiderpmsi.jpmi2stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.commons.scxml.SCXMLExecutor;
import org.apache.commons.scxml.env.SimpleDispatcher;
import org.apache.commons.scxml.env.SimpleErrorReporter;
import org.apache.commons.scxml.env.jexl.JexlContext;
import org.apache.commons.scxml.env.jexl.JexlEvaluator;
import org.apache.commons.scxml.io.SCXMLParser;
import org.apache.commons.scxml.model.CustomAction;
import org.apache.commons.scxml.model.ModelException;
import org.apache.commons.scxml.model.SCXML;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.github.aiderpmsi.jpmsi2stream.linestypes.EndOfFile;
import com.github.aiderpmsi.jpmsi2stream.linestypes.PmsiRsf2012Header;
import com.github.aiderpmsi.jpmsi2stream.linestypes.PmsiRsf2012a;
import com.github.aiderpmsi.jpmsi2stream.linestypes.PmsiRsf2012b;
import com.github.aiderpmsi.jpmsi2stream.linestypes.PmsiRsf2012c;
import com.github.aiderpmsi.jpmsi2stream.linestypes.PmsiRsf2012h;
import com.github.aiderpmsi.jpmsi2stream.linestypes.PmsiRsf2012l;
import com.github.aiderpmsi.jpmsi2stream.linestypes.PmsiRsf2012m;

public class ExecutorFactory {

	private URL scxmlDocument = null;

	private ErrorHandler errorHandler = null;

	private List<CustomAction> customActions = null;
	
	private MemoryBufferedReader memoryBufferedReader = null;

	public URL getScxmlDocument() {
		return scxmlDocument;
	}

	public ExecutorFactory setScxmlDocument(URL scxmlDocument) {
		this.scxmlDocument = scxmlDocument;
		return this;
	}

	public ErrorHandler getErrorHandler() {
		return errorHandler;
	}

	public ExecutorFactory setErrorHandler(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
		return this;
	}

	public List<CustomAction> getCustomActions() {
		return customActions;
	}

	public ExecutorFactory setCustomActions(List<CustomAction> customActions) {
		this.customActions = customActions;
		return this;
	}

	public MemoryBufferedReader getMemoryBufferedReader() {
		return memoryBufferedReader;
	}

	public ExecutorFactory setMemoryBufferedReader(MemoryBufferedReader memoryBufferedReader) {
		this.memoryBufferedReader = memoryBufferedReader;
		return this;
	}

	public SCXMLExecutor createMachine() throws IOException, SAXException,
			ModelException {
		
		URLConnection connection = scxmlDocument.openConnection();
		connection.setUseCaches(false);
		InputStream stream = connection.getInputStream();
		InputSource source = new InputSource(stream);
		source.setSystemId(scxmlDocument.toExternalForm());

		SCXML scxml = SCXMLParser.parse(source, errorHandler, customActions);
		
		JexlContext appCtx = new JexlContext();
		appCtx.set("_file", memoryBufferedReader);
		appCtx.set("_line_rsf2012header", new PmsiRsf2012Header());
		appCtx.set("_line_rsf2012a", new PmsiRsf2012a());
		appCtx.set("_line_rsf2012b", new PmsiRsf2012b());
		appCtx.set("_line_rsf2012c", new PmsiRsf2012c());
		appCtx.set("_line_rsf2012h", new PmsiRsf2012h());
		appCtx.set("_line_rsf2012l", new PmsiRsf2012l());
		appCtx.set("_line_rsf2012m", new PmsiRsf2012m());
		appCtx.set("_line_eof", new EndOfFile());
		appCtx.set("_contenthandler", new DefaultContentHandler(new PrintWriter(System.out)));

		SCXMLExecutor engine = new SCXMLExecutor(
				new JexlEvaluator(), new SimpleDispatcher(), new SimpleErrorReporter());
		engine.setStateMachine(scxml);
		engine.setSuperStep(true);
		engine.setRootContext(appCtx);
		
		return engine;
	}

}
