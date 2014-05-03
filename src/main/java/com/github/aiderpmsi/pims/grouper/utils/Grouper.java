package com.github.aiderpmsi.pims.grouper.utils;

import java.io.InputStream;

import org.apache.commons.scxml.SCXMLExecutor;
import org.apache.commons.scxml.model.ModelException;
import org.xml.sax.ext.DefaultHandler2;
import com.github.aiderpmsi.pims.grouper.model.RssContent;

public class Grouper {

	// scxml location
	private static final String scxmlLocation = "grouper.xml";

	public String group(RssContent rss) throws Exception {

		try {
			// SOURCE OF THE STATE MACHINE DEFINITION
			InputStream scxmlSource = this.getClass().getClassLoader().getResourceAsStream(scxmlLocation);

			ExecutorFactory machineFactory = new ExecutorFactory()
				.setScxmlSource(scxmlSource)
				.setErrorHandler(new DefaultHandler2())
				.setRss(rss);

			SCXMLExecutor machine = machineFactory.createMachine();

			// RUN THE STATE MACHINE
			machine.go();
			
			// GETS THE MACHINE RESULT
			String result = (String) machine.getRootContext().get("_result");
			return result;
			
		} catch (ModelException e) {
			throw new Exception(e);
		}
	}

}
