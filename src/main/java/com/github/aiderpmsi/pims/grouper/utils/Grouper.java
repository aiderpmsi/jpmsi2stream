package com.github.aiderpmsi.pims.grouper.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

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

public class Grouper implements Callable<Boolean> {

	// XML TREE
	private static final String treeLocation = "grouper.xml";
	
	// DOM TREE
	private static Document document = null;

	// LOCK THE DOCUMENT
	private static ReentrantLock lock = new ReentrantLock(); 
	
	// STORES THE LAST TIMESTAMP WHEN DOCUMENT WAS USED
	private static Long lastused = null;
	
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

	private Document getDocument() throws ParserConfigurationException, SAXException, IOException {
		lock.lock();
		
		try {
			Document to_ret;
			if (document != null) {
				to_ret = (Document) document.cloneNode(true);
			} else {
				DocumentBuilderFactory docbfactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = docbfactory.newDocumentBuilder();
	
				// TREE DEFINITION
				InputStream treeSource = this.getClass().getClassLoader().getResourceAsStream(treeLocation);
	
				document = builder.parse(treeSource);
				to_ret = (Document) document.cloneNode(true);
				
				// START THREAD AWAITING CLEANING
				Executors.newSingleThreadExecutor().submit(new Grouper());
			}
			lastused = (new Date()).getTime();
			return to_ret;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public Boolean call() throws Exception {
		// WAITS UNTIL DOCUMENT HAS NOT BEEN ACCESSED DURING 10 MINS (6000000 MILLIS)
		while (true) {
			// SEE IF WE HAVE TO QUIT
			lock.lock();
			try {
				if (Thread.interrupted() || (new Date()).getTime() - lastused > 6000000) {
					lastused = null;
					document = null;
					break;
				}
			} finally {
				lock.unlock();
			}

			// SLEEP
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// WAIT UNTIL LOCK AND SEE IF WE HAVE TO QUIT
			}
		}
		return true;
	}
	
}
