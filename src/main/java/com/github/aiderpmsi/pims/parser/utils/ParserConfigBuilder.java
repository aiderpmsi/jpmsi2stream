package com.github.aiderpmsi.pims.parser.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.jexl2.JexlEngine;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.github.aiderpmsi.pims.parser.linestypes.LineDictionary;
import com.github.aiderpmsi.pims.treebrowser.ConfigBuilder;
import com.github.aiderpmsi.pims.treebrowser.TreeBrowserException;

public class ParserConfigBuilder implements ConfigBuilder<ParserConfig> {

	private static final String treeLocation = "com/github/aiderpmsi/pims/parser/pims.xml";

	private DocumentBuilder dBuilder;
	
	public ParserConfigBuilder() throws TreeBrowserException {
		try {
			dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new TreeBrowserException(e);
		}
	}
	
	@Override
	public ParserConfig build() throws TreeBrowserException {
		ParserConfig config = new ParserConfig();

		config.setClonedTime(null);
		
		HashMap<String, Object> context = new HashMap<>();
		LineDictionary dicos = new LineDictionary();
		context.put("utils", new Utils(dicos));
		context.put("dicos", dicos);
		config.setContext(context);
		
		config.setJexlEngine(new JexlEngine());
		
		try {
			InputStream treeSource = this.getClass().getClassLoader().getResourceAsStream(treeLocation);
			Document tree = dBuilder.parse(treeSource);
			config.setTree(tree);
		} catch (SAXException | IOException e) {
			throw new TreeBrowserException(e);
		}
		
		return config;
	}

}
