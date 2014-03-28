package com.github.aiderpmsi.pims.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

import com.github.aiderpmsi.pims.customtags.ErrorInvocator;
import com.github.aiderpmsi.pims.customtags.LineInvocator;
import com.github.aiderpmsi.pims.customtags.LineWriter;
import com.github.aiderpmsi.pims.customtags.NumLineWriter;
import com.github.aiderpmsi.pims.customtags.Print;
import com.github.aiderpmsi.pims.linestypes.LineDictionary;

public class ExecutorFactory {

	/**
	 * Instructions du scxml
	 */
	private InputStream scxmlSource = null;
	
	/**
	 * Lecture du fichier source (doit permettre de lire plusieurs fois la même ligne
	 * pour tester les différentes solutions)
	 */
	private MemoryBufferedReader memoryBufferedReader = null;
		
	/**
	 * Gestionnaire du contenu du fichier source
	 */
	private ContentHandler contentHandler;

	/**
	 * Gestion des erreurs générées lors de la lecture du fichier source pmsi
	 */
	private ErrorHandler errorHandler;
	
	public InputStream getScxmlSource() {
		return scxmlSource;
	}

	public ExecutorFactory setScxmlSource(InputStream scxmlSource) {
		this.scxmlSource = scxmlSource;
		return this;
	}

	public MemoryBufferedReader getMemoryBufferedReader() {
		return memoryBufferedReader;
	}

	public ExecutorFactory setMemoryBufferedReader(MemoryBufferedReader memoryBufferedReader) {
		this.memoryBufferedReader = memoryBufferedReader;
		return this;
	}

	public ContentHandler getContentHandler() {
		return contentHandler;
	}

	public ExecutorFactory setContentHandler(ContentHandler contentHandler) {
		this.contentHandler = contentHandler;
		return this;
	}
	
	public ErrorHandler getErrorHandler() {
		return errorHandler;
	}

	public ExecutorFactory setErrorHandler(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
		return this;
	}

	public SCXMLExecutor createMachine() throws IOException, SAXException,
			ModelException {
		// Sets the scxml document
		InputSource source = new InputSource(scxmlSource);

		// Sets the custom tags
		List<CustomAction> customActions = new ArrayList<CustomAction>();
		customActions.add(new CustomAction(
				"http://my.custom-actions.domain/CUSTOM", "lineinvocator",
				LineInvocator.class));
		customActions.add(new CustomAction(
				"http://my.custom-actions.domain/CUSTOM", "linewriter",
				LineWriter.class));
		customActions
				.add(new CustomAction("http://my.custom-actions.domain/CUSTOM",
						"print", Print.class));
		customActions.add(new CustomAction(
				"http://my.custom-actions.domain/CUSTOM", "numlinewriter",
				NumLineWriter.class));
		customActions.add(new CustomAction(
				"http://my.custom-actions.domain/CUSTOM", "error",
				ErrorInvocator.class));

		// Sets the scxml definition
		SCXML scxml = SCXMLParser.parse(source, new DefaultHandler2(), customActions);
		// Sets the machine context
		JexlContext appCtx = new JexlContext();
		appCtx.set("_file", memoryBufferedReader);
		appCtx.set("_dictionary", new LineDictionary());
		appCtx.set("_contenthandler", contentHandler);
		appCtx.set("_errorhandler", getErrorHandler());
		
		// Creates the engine
		SCXMLExecutor engine = new SCXMLExecutor(
				new JexlEvaluator(), new SimpleDispatcher(), new SimpleErrorReporter());
		engine.setStateMachine(scxml);
		engine.setSuperStep(true);
		engine.setRootContext(appCtx);
		
		return engine;
	}

}
