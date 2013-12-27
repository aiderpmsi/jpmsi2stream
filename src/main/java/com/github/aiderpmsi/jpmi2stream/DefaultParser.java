package com.github.aiderpmsi.jpmi2stream;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.scxml.SCXMLExecutor;
import org.apache.commons.scxml.model.CustomAction;
import org.xml.sax.helpers.DefaultHandler;

import com.github.aiderpmsi.jpmi2stream.customtags.LineInvocator;
import com.github.aiderpmsi.jpmi2stream.customtags.LineWriter;

public class DefaultParser {

	public static void main(String[] args) {
		List<CustomAction> customActions = new ArrayList<CustomAction>();
		customActions.add(new CustomAction(
				"http://my.custom-actions.domain/CUSTOM", "lineinvocator", LineInvocator.class));
		customActions.add(new CustomAction(
				"http://my.custom-actions.domain/CUSTOM", "linewriter", LineWriter.class));

		// (2) Parse the SCXML document containing the custom action(s)
		try {
			URL scxmlLocation = new URL(null, "classpath:test.scxml",
					new ClasspathHandler(ClassLoader.getSystemClassLoader()));
			ExecutorFactory machineFactory = new ExecutorFactory()
					.setScxmlDocument(scxmlLocation)
					.setCustomActions(customActions)
					.setErrorHandler(new DefaultHandler());
			SCXMLExecutor machine = machineFactory.createMachine();
			machine.go();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
