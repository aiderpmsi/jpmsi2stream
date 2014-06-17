package com.github.aiderpmsi.pims.grouper.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.MapContext;

import com.github.aiderpmsi.pims.grouper.model.Dictionaries;
import com.github.aiderpmsi.pims.grouper.model.RssActe;
import com.github.aiderpmsi.pims.grouper.model.RssContent;
import com.github.aiderpmsi.pims.grouper.model.RssDa;
import com.github.aiderpmsi.pims.grouper.model.RssMain;
import com.github.aiderpmsi.pims.grouper.model.Utils;
import com.github.aiderpmsi.pims.grouper.tags.GroupFactory;
import com.github.aiderpmsi.pims.treebrowser.TreeBrowser;
import com.github.aiderpmsi.pims.treebrowser.TreeBrowserException;
import com.github.aiderpmsi.pims.treebrowser.actions.ActionFactory;
import com.github.aiderpmsi.pims.treebrowser.actions.ActionFactory.Action;
import com.github.aiderpmsi.pims.treemodel.Node;

public class Grouper {

	private Node<ActionFactory.Action> tree;
	
	private Dictionaries dicos = new Dictionaries();
	
	@SuppressWarnings("unchecked")
	public Grouper() throws TreeBrowserException {
		GrouperConfigBuilder gcb = new GrouperConfigBuilder();
		tree = (Node<Action>) gcb.build();
	}
	
	public GroupFactory.Group group(List<RssContent> multirss) throws IOException {
		// GETS THE MIXED RSS
		Mixer mixer = new Mixer();
		mixer.setDicos(dicos);
		RssContent rss = mixer.mix(multirss);

		// CREATES THE VARS MAP
		HashMap<String, Object> context = new HashMap<>();
		context.put("utils", new Utils(dicos));
		context.put("dicos", dicos);
		context.put("rss", rss);
		context.put("rssmain", RssMain.dp);
		context.put("rssacte", RssActe.codeccam);
		context.put("rssda", RssDa.da);
		JexlContext jc = new MapContext(context);

		// CREATES AND EXECUTES THE TREE BROWSER
		TreeBrowser tb = new TreeBrowser(tree);
		tb.setJc(jc);

		// LAUNCHES THE BROWSER
		tb.go();
		
		// GETS THE MACHINE RESULT
		GroupFactory.Group result = (GroupFactory.Group) tb.getJc().get("group");
		
		return result;
	}

}
