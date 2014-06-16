package com.github.aiderpmsi.pims.grouper.utils;

import java.util.List;

import javax.script.ScriptContext;
import javax.script.SimpleScriptContext;

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
	
	public GroupFactory.Group group(List<RssContent> multirss) throws Exception {
		// GETS THE MIXED RSS
		Mixer mixer = new Mixer();
		mixer.setDicos(dicos);
		RssContent rss = mixer.mix(multirss);

		// CREATES THE VARS MAP
		ScriptContext sc = new SimpleScriptContext();
		sc.setAttribute("utils", new Utils(dicos), ScriptContext.ENGINE_SCOPE);
		sc.setAttribute("dicos", dicos, ScriptContext.ENGINE_SCOPE);
		sc.setAttribute("rss", rss, ScriptContext.ENGINE_SCOPE);
		sc.setAttribute("rssmain", RssMain.dp, ScriptContext.ENGINE_SCOPE);
		sc.setAttribute("rssacte", RssActe.codeccam, ScriptContext.ENGINE_SCOPE);
		sc.setAttribute("rssda", RssDa.da, ScriptContext.ENGINE_SCOPE);

		// CREATES AND EXECUTES THE TREE BROWSER
		TreeBrowser tb = new TreeBrowser(tree);
		tb.setContext(sc);

		// LAUNCHES THE BROWSER
		tb.go();
		
		// GETS THE MACHINE RESULT
		GroupFactory.Group result = (GroupFactory.Group) tb.getContext().getAttribute("group");
		
		return result;
	}

}
