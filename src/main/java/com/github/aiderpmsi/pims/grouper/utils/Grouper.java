package com.github.aiderpmsi.pims.grouper.utils;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.github.aiderpmsi.pims.grouper.customtags.Assign;
import com.github.aiderpmsi.pims.grouper.customtags.Execute;
import com.github.aiderpmsi.pims.grouper.customtags.Group;
import com.github.aiderpmsi.pims.grouper.customtags.Move;
import com.github.aiderpmsi.pims.grouper.customtags.Switch;
import com.github.aiderpmsi.pims.grouper.model.Dictionaries;
import com.github.aiderpmsi.pims.grouper.model.RssContent;
import com.github.aiderpmsi.pims.grouper.model.Utils;

public class Grouper {

	// XML TREE
	private static final String treeLocation = "grouper.xml";
	
	// DOM TREE
	private static Document document = null;

	public Group group(RssContent rss) throws Exception {

		// GETS THE DOCUMENT
		Document thisDocument = getDocument();
		
		// CREATES THE DOM BROWSER
		TreeBrowser tb = new TreeBrowser();
		tb.setDOM(thisDocument);
		tb.addDataModel("rss", rss);
		tb.addDataModel("utils", new Utils(new Dictionaries("grouper-", ".cfg")));
		tb.AddAction("http://default.actions/default", "execute", Execute.class);
		tb.AddAction("http://default.actions/default", "assign", Assign.class);
		tb.AddAction("http://default.actions/default", "switch", Switch.class);
		tb.AddAction("http://default.actions/default", "move", Move.class);
		tb.AddAction("http://custom.actions/pims", "group", Group.class);
		tb.go();
		// GETS THE MACHINE RESULT
		Group result = (Group) tb.getDataModel("group");
		
		return result;
	}

	private synchronized Document getDocument() throws ParserConfigurationException, SAXException, IOException {
		if (document != null) {
			return (Document) document.cloneNode(true);
		} else {
			DocumentBuilderFactory docbfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = docbfactory.newDocumentBuilder();

			// TREE DEFINITION
			InputStream treeSource = this.getClass().getClassLoader().getResourceAsStream(treeLocation);

			document = builder.parse(treeSource);
			return (Document) document.cloneNode(true);
		}
	}
}
