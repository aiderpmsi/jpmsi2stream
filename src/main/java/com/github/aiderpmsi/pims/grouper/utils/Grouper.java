package com.github.aiderpmsi.pims.grouper.utils;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import com.github.aiderpmsi.pims.grouper.customtags.Assign;
import com.github.aiderpmsi.pims.grouper.customtags.Execute;
import com.github.aiderpmsi.pims.grouper.customtags.Group;
import com.github.aiderpmsi.pims.grouper.customtags.Switch;
import com.github.aiderpmsi.pims.grouper.model.RssContent;

public class Grouper {

	// scxml location
	private static final String treeLocation = "grouper.xml";

	public Group group(RssContent rss) throws Exception {
		
		Document document = null;
		DocumentBuilderFactory docbfactory = null;
		
		docbfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = docbfactory.newDocumentBuilder();

		// TREE DEFINITION
		InputStream treeSource = this.getClass().getClassLoader().getResourceAsStream(treeLocation);

		// CREATES THE DOM
		document = builder.parse(treeSource);
		
		// CREATES THE DOM BROWSER
		TreeBrowser tb = new TreeBrowser();
		tb.setDOM(document);
		tb.addDataModel("rss", rss);
		tb.AddAction("http://default.actions/default", "execute", Execute.class);
		tb.AddAction("http://default.actions/default", "assign", Assign.class);
		tb.AddAction("http://default.actions/default", "switch", Switch.class);
		tb.go();
		// GETS THE MACHINE RESULT
		Group result = (Group) tb.getDataModel("group");
		
		return result;
	}

}
