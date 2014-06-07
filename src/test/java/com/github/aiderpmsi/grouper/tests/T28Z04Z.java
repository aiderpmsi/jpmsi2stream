package com.github.aiderpmsi.grouper.tests;

import com.github.aiderpmsi.pims.grouper.model.RssActe;
import com.github.aiderpmsi.pims.grouper.model.RssContent;
import com.github.aiderpmsi.pims.grouper.model.RssDa;
import com.github.aiderpmsi.pims.grouper.model.RssMain;

public class T28Z04Z extends BaseTest {

	private static final long serialVersionUID = -3200244301420815556L;

	private Object[][] rssmain = { { RssMain.dp, "Z491     " },
			{ RssMain.dr, "B202    " }, { RssMain.nbseances, "1      " },
			{ RssMain.modesortie, "9 " }, { RssMain.ddn, "1980-04-05" },
			{ RssMain.dateentree, "2013-04-08" },
			{ RssMain.datesortie, "2013-12-15" } };

	private Object[][][] acte = {
			{ { RssActe.codeccam, "JVJF004 " }, { RssActe.phase, "0" } },
			{ { RssActe.codeccam, "AAJA001 " }, { RssActe.phase, "0" } }};

	private Object[][][] da = { { { RssDa.da, "A280" }, },
			{ { RssDa.da, "Z491" } } };

	public T28Z04Z() {
		RssContent rss = buildRssContent(rssmain, acte, da);
		add(rss);
	}

}
