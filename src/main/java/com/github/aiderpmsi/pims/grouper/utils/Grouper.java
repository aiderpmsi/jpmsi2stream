package com.github.aiderpmsi.pims.grouper.utils;

import java.util.List;

import com.github.aiderpmsi.pims.grouper.model.Dictionaries;
import com.github.aiderpmsi.pims.grouper.model.RssContent;
import com.github.aiderpmsi.pims.grouper.tags.Group;
import com.github.aiderpmsi.pims.treebrowser.TreeBrowser;
import com.github.aiderpmsi.pims.treebrowser.TreeBrowserException;
import com.github.aiderpmsi.pims.treebrowser.TreeBrowserFactory;

public class Grouper {

	private TreeBrowserFactory<GrouperConfig> tbf;
	
	public Grouper() throws TreeBrowserException {
		GrouperConfigBuilder gcb = new GrouperConfigBuilder();
		this.tbf = new TreeBrowserFactory<>(gcb);
	}
	
	public Group group(List<RssContent> multirss) throws Exception {
		// GETS THE DOCUMENT AND DICO
		TreeBrowser tb = tbf.build();
		Dictionaries dicos = (Dictionaries) tb.getJc().get("dicos");

		// GETS THE MIXED RSS
		Mixer mixer = new Mixer();
		mixer.setDicos(dicos);
		RssContent rss = mixer.mix(multirss);
		
		// CREATES THE DOM BROWSER
		tb.getJc().set("rss", rss);
		tb.addAction("http://custom.actions/pims", "group", new Group());

		// LAUNCHES THE BROWSER
		tb.go();
		
		// GETS THE MACHINE RESULT
		Group result = (Group) tb.getJc().get("group");
		
		return result;
	}

}
