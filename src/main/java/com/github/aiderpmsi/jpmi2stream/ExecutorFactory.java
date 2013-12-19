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

import aider.org.pmsi.parser.linestypes.PmsiRsf2012Header;

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
		
		SCXMLExecutor engine =
				new SCXMLExecutor(new JexlEvaluator(),
						new SimpleDispatcher(),
						new SimpleErrorReporter());
		engine.setStateMachine(scxml);
		engine.setSuperStep(true);
		
		JexlContext appCtx = new JexlContext();
		appCtx.set("_file", memoryBufferedReader);
		appCtx.set("_rsf2012Header", new PmsiRsf2012Header());
		appCtx.set("_contentHandler", new DefaultContentHandler(new PrintWriter(System.out)));
		engine.setRootContext(appCtx);

		return engine;
	}

}
