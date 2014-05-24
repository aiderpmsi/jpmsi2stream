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

import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.github.aiderpmsi.pims.grouper.model.Dictionaries;
import com.github.aiderpmsi.pims.grouper.model.RssContent;
import com.github.aiderpmsi.pims.grouper.model.Utils;
import com.github.aiderpmsi.pims.grouper.tags.Group;
import com.github.aiderpmsi.pims.treebrowser.TreeBrowser;

public class Grouper implements Callable<Boolean> {

	public class StaticElements {
		// DOM TREE
		public Document document = null;
		// DICTIONARIES
		public Dictionaries dicos = null;
		// LAST DATE USE OF STATIC ELEMENTS
		public Long lastused = null;
		// JEXL EXECUTOR
		public JexlEngine jexl = null;
	}
	
	// XML TREE
	private static final String treeLocation = "com/github/aiderpmsi/pims/grouper/grouper.xml";
	
	// STATIC ELEMENTS
	private static StaticElements elts = null;

	// LOCK THE DOCUMENT
	private static ReentrantLock lock = new ReentrantLock(); 
	
	public Grouper() {
	}
	
	public Group group(List<RssContent> multirss) throws Exception {
		// GETS THE DOCUMENT AND DICO
		StaticElements thiselements = getStaticElements();

		// GETS THE MIXED RSS
		Mixer mixer = new Mixer();
		mixer.setDicos(thiselements.dicos);
		RssContent rss = mixer.mix(multirss);
		
		// CREATES THE DOM BROWSER
		TreeBrowser tb = new TreeBrowser();
		tb.setTree(thiselements.document);
		MapContext mc = new MapContext();
		mc.set("rss", rss);
		mc.set("utils", new Utils(thiselements.dicos));
		tb.setJc(mc);
		tb.addAction("http://custom.actions/pims", "group", new Group());
		tb.setJexl(thiselements.jexl);
		tb.go();
		// GETS THE MACHINE RESULT
		Group result = (Group) tb.getJc().get("group");
		
		return result;
	}

	private StaticElements getStaticElements() throws ParserConfigurationException, SAXException, IOException {
		lock.lock();
		
		try {
			if (elts == null) {
				// CREATE A NEW ELEMENT
				elts = new StaticElements();
				
				// CREATE THE DOCUMENT
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputStream treeSource = this.getClass().getClassLoader().getResourceAsStream(treeLocation);
				elts.document = builder.parse(treeSource);

				// CREATES THE DICTIONARIES
				elts.dicos = new Dictionaries("com/github/aiderpmsi/pims/grouper/grouper-", ".cfg");

				// CREATES THE JEXL EXECUTOR
				elts.jexl = new JexlEngine();
				
				// START THREAD AWAITING CLEANING
				Executors.newSingleThreadExecutor().submit(new Grouper());
			}

			// CLONES THE STATIC ELEMENTS
			StaticElements clonedElts = new StaticElements();
			clonedElts.document = (Document) elts.document.cloneNode(true);
			clonedElts.dicos = elts.dicos;
			clonedElts.jexl = elts.jexl;
			clonedElts.lastused = elts.lastused;

			elts.lastused = (new Date()).getTime();
			return clonedElts;

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
				if (Thread.interrupted() || (new Date()).getTime() - elts.lastused > 6000000) {
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
