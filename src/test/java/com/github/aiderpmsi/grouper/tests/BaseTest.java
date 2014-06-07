package com.github.aiderpmsi.grouper.tests;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

import com.github.aiderpmsi.pims.grouper.model.RssActe;
import com.github.aiderpmsi.pims.grouper.model.RssContent;
import com.github.aiderpmsi.pims.grouper.model.RssDa;
import com.github.aiderpmsi.pims.grouper.model.RssMain;

public class BaseTest extends ArrayList<RssContent> {
	
	private static final long serialVersionUID = -5321260412364005350L;

	private EnumMapBuilder<RssMain, String> rssMainBuilder = new EnumMapBuilder<RssMain, String>(RssMain.class);
	private EnumMapBuilder<RssActe, String> rssActeBuilder = new EnumMapBuilder<RssActe, String>(RssActe.class);
	private EnumMapBuilder<RssDa, String> rssDaBuilder = new EnumMapBuilder<RssDa, String>(RssDa.class);
	
	protected EnumMap<RssMain, String> fillMain(Object[][] values) {
		List<EnumMapBuilder<RssMain, String>.Entry> entries = new LinkedList<>();
		for (Object[] value : values) {
			entries.add(rssMainBuilder.buildEntry((RssMain) value[0], (String) value[1]));
		}
		return rssMainBuilder.buildAndfill(entries);
	}
	
	protected EnumMap<RssActe, String> fillActe(Object[][] values) {
		List<EnumMapBuilder<RssActe, String>.Entry> entries = new LinkedList<>();
		for (Object[] value : values) {
			entries.add(rssActeBuilder.buildEntry((RssActe) value[0], (String) value[1]));
		}
		return rssActeBuilder.buildAndfill(entries);
	}

	protected EnumMap<RssDa, String> fillDa(Object[][] values) {
		List<EnumMapBuilder<RssDa, String>.Entry> entries = new LinkedList<>();
		for (Object[] value : values) {
			entries.add(rssDaBuilder.buildEntry((RssDa) value[0], (String) value[1]));
		}
		return rssDaBuilder.buildAndfill(entries);
	}
	
	protected RssContent buildRssContent(Object[][] main, Object[][][] actes, Object[][][] das) {
		RssContent rsscontent = new RssContent();

		rsscontent.setRssmain(fillMain(main));
		
		List<EnumMap<RssActe, String>> rssActes = new ArrayList<>();
 		for (Object[][] acte : actes) {
			rssActes.add(fillActe(acte));
		}
 		rsscontent.setRssacte(rssActes);
		
		List<EnumMap<RssDa, String>> rssDas = new ArrayList<>();
 		for (Object[][] da : das) {
			rssDas.add(fillDa(da));
		}
 		rsscontent.setRssda(rssDas);

 		return rsscontent;
	}
	
}
