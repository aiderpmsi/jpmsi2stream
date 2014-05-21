package com.github.aiderpmsi.pims.grouper.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.github.aiderpmsi.pims.grouper.model.Dictionaries;
import com.github.aiderpmsi.pims.grouper.model.RssContent;
import com.github.aiderpmsi.pims.grouper.model.Utils;
import com.github.aiderpmsi.pims.grouper.tags.Assign;
import com.github.aiderpmsi.pims.grouper.tags.Execute;
import com.github.aiderpmsi.pims.grouper.tags.Group;
import com.github.aiderpmsi.pims.grouper.tags.Move;
import com.github.aiderpmsi.pims.grouper.tags.Switch;

public class Grouper implements Callable<Boolean> {

	public class StaticElements {
		// DOM TREE
		public Document document = null;
		// DICTIONARIES
		public Dictionaries dicos = null;
	}
	
	// XML TREE
	private static final String treeLocation = "grouper.xml";
	
	// STATIC ELEMENTS
	private static StaticElements elts = null;

	// LOCK THE DOCUMENT
	private static ReentrantLock lock = new ReentrantLock(); 
	
	// STORES THE LAST TIMESTAMP WHEN DOCUMENT WAS USED
	private static Long lastused = null;
	
	public Group group(List<RssContent> multirss) throws Exception {
		// GETS THE DOCUMENT AND DICO
		StaticElements thiselements = getStaticElements();

		// GETS THE MIXED RSS
		Mixer mixer = new Mixer();
		mixer.setDicos(thiselements.dicos);
		RssContent rss = mixer.mix(multirss);
		
		// CREATES THE DOM BROWSER
		TreeBrowser tb = new TreeBrowser();
		tb.setDOM(thiselements.document);
		tb.addDataModel("rss", rss);
		tb.addDataModel("utils", new Utils(thiselements.dicos));
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

	private StaticElements getStaticElements() throws ParserConfigurationException, SAXException, IOException {
		lock.lock();
		
		try {
			StaticElements to_ret = new StaticElements();
			if (elts != null) {
				to_ret.document = (Document) elts.document.cloneNode(true);
				to_ret.dicos = elts.dicos;
			} else {
				elts = new StaticElements();
				DocumentBuilderFactory docbfactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = docbfactory.newDocumentBuilder();
	
				// TREE DEFINITION
				InputStream treeSource = this.getClass().getClassLoader().getResourceAsStream(treeLocation);
	
				elts.document = builder.parse(treeSource);
				to_ret.document = (Document) elts.document.cloneNode(true);
				
				// CREATES THE DICTIONARIES
				elts.dicos = new Dictionaries("com/github/aiderpmsi/pims/grouper/grouper-", ".cfg");
				to_ret.dicos = elts.dicos;
				
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
					elts = null;
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
