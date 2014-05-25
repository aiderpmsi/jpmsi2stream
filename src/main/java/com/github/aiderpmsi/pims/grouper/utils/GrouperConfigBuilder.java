package com.github.aiderpmsi.pims.grouper.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.jexl2.JexlEngine;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.github.aiderpmsi.pims.grouper.model.Dictionaries;
import com.github.aiderpmsi.pims.grouper.model.Utils;
import com.github.aiderpmsi.pims.treebrowser.ConfigBuilder;
import com.github.aiderpmsi.pims.treebrowser.TreeBrowserException;

public class GrouperConfigBuilder implements ConfigBuilder<GrouperConfig> {

	private static final String treeLocation = "com/github/aiderpmsi/pims/grouper/grouper.xml";

	@Override
	public GrouperConfig build() throws TreeBrowserException {
		GrouperConfig config = new GrouperConfig();

		config.setClonedTime(null);
		
		HashMap<String, Object> context = new HashMap<>();
		Dictionaries dicos = new Dictionaries("com/github/aiderpmsi/pims/grouper/grouper-", ".cfg");
		context.put("utils", new Utils(dicos));
		context.put("dicos", dicos);
		config.setContext(context);
		
		config.setJexlEngine(new JexlEngine());
		
		DocumentBuilder builder;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputStream treeSource = this.getClass().getClassLoader().getResourceAsStream(treeLocation);
			Document tree = builder.parse(treeSource);
			config.setTree(tree);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new TreeBrowserException(e);
		}
		
		return config;
	}

}
