package com.github.aiderpmsi.pims.grouper.utils;

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
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

import com.github.aiderpmsi.pims.grouper.customtags.Group;
import com.github.aiderpmsi.pims.grouper.customtags.IsInList;
import com.github.aiderpmsi.pims.grouper.customtags.RssActe;
import com.github.aiderpmsi.pims.grouper.customtags.RssMain;
import com.github.aiderpmsi.pims.grouper.model.GrouperDictionary;
import com.github.aiderpmsi.pims.grouper.model.RssContent;

public class ExecutorFactory {

	/**
	 * Instructions du scxml
	 */
	private InputStream scxmlSource = null;
	
	/**
	 * Ligne du rss
	 */
	private RssContent rss;
	
	/**
	 * Gestion des erreurs générées lors du groupage du rss
	 */
	private ErrorHandler errorHandler;

		
	public InputStream getScxmlSource() {
		return scxmlSource;
	}

	public ExecutorFactory setScxmlSource(InputStream scxmlSource) {
		this.scxmlSource = scxmlSource;
		return this;
	}

	public RssContent getRss() {
		return rss;
	}

	public ExecutorFactory setRss(RssContent rss) {
		this.rss = rss;
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
				"http://my.custom-actions.domain/CUSTOM", "rssMain",
				RssMain.class));
		customActions.add(new CustomAction(
				"http://my.custom-actions.domain/CUSTOM", "rssActe",
				RssActe.class));
		customActions.add(new CustomAction(
				"http://my.custom-actions.domain/CUSTOM", "isInList",
				IsInList.class));
		customActions.add(new CustomAction(
				"http://my.custom-actions.domain/CUSTOM", "group",
				Group.class));

		// Sets the scxml definition
		SCXML scxml = SCXMLParser.parse(source, new DefaultHandler2(), customActions);
		// Sets the machine context
		JexlContext appCtx = new JexlContext();
		appCtx.set("_dictionary", new GrouperDictionary());
		appCtx.set("_rssContent", getRss());
		
		// Creates the engine
		SCXMLExecutor engine = new SCXMLExecutor(
				new JexlEvaluator(), new SimpleDispatcher(), new SimpleErrorReporter());
		engine.setStateMachine(scxml);
		engine.setSuperStep(true);
		engine.setRootContext(appCtx);
		
		return engine;
	}

}
