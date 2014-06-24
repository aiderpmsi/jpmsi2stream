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
import com.github.aiderpmsi.pims.treebrowser.TreeBrowser;
import com.github.aiderpmsi.pims.treebrowser.TreeBrowserException;
import com.github.aiderpmsi.pims.treebrowser.actions.IActionFactory;
import com.github.aiderpmsi.pims.treebrowser.actions.IActionFactory.IAction;
import com.github.aiderpmsi.pims.treemodel.Node;

public class Grouper {

	private Node<IActionFactory.IAction> tree;
	
	private Dictionaries dicos = new Dictionaries();
	
	@SuppressWarnings("unchecked")
	public Grouper() throws TreeBrowserException {
		GrouperConfigBuilder gcb = new GrouperConfigBuilder();
		tree = (Node<IAction>) gcb.build();
	}
	
	/**
	 * Groups a list of {@link RssContent}
	 * @param multirss
	 * @return
	 * @throws IOException
	 */
	public HashMap<?, ?> group(List<RssContent> multirss) throws IOException {
		// GETS THE MIXED RSS
		Mixer mixer = new Mixer(dicos);
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

		// LAUNCHES THE BROWSER, GETTING ALL EXCEPTIONS IF NEEDED
		try {
			tb.go();
		} catch (Exception e) {
			throw new IOException(e);
		}
		
		// GETS THE MACHINE RESULT
		return (HashMap<?, ?>) tb.getJc().get("group");
	}

}
