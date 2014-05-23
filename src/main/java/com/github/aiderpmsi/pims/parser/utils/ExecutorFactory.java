package com.github.aiderpmsi.pims.parser.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.Script;
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

import com.github.aiderpmsi.pims.parser.customtags.Utils;
import com.github.aiderpmsi.pims.parser.customtags.Execute;
import com.github.aiderpmsi.pims.parser.linestypes.LineDictionary;

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

		// SETS THE CUSTOM EXECUTE TAG
		List<CustomAction> customActions = new ArrayList<CustomAction>();
		// CREATES THE JEXL2EXECUTOR
		JexlEngine jexl2 = new JexlEngine();
		// CREATES THE CACHED EXPRESSIONS
		HashMap<String, Script> scripts = new HashMap<>();
		customActions.add(new CustomAction(
				"http://custom.actions/pims", "execute",
				Execute.class));
		
		// SETS THE SCXML DEFINITION
		SCXML scxml = SCXMLParser.parse(source, new DefaultHandler2(), customActions);

		// SETS THE MACHINE CONTEXT
		JexlContext appCtx = new JexlContext();
		appCtx.set("utils", new Utils(memoryBufferedReader, new LineDictionary(), getErrorHandler(), contentHandler));
		appCtx.set("scripts", scripts);
		appCtx.set("jexl2", jexl2);
		
		// Creates the engine
		SCXMLExecutor engine = new SCXMLExecutor(
				new JexlEvaluator(), new SimpleDispatcher(), new SimpleErrorReporter());
		engine.setStateMachine(scxml);
		engine.setSuperStep(true);
		engine.setRootContext(appCtx);
		
		return engine;
	}

}
