package com.github.aiderpmsi.grouper.tests;

import com.github.aiderpmsi.pims.grouper.model.RssActe;
import com.github.aiderpmsi.pims.grouper.model.RssContent;
import com.github.aiderpmsi.pims.grouper.model.RssDa;
import com.github.aiderpmsi.pims.grouper.model.RssMain;

public class T15Z10E extends BaseTest {

	private static final long serialVersionUID = 8240003977349009665L;

	private Object[][] rssmain = { { RssMain.dp, "P95      " },
			{ RssMain.dr, "B202    " }, { RssMain.nbseances, "0      " },
			{ RssMain.modesortie, "9 " }, { RssMain.ddn, "05042013" },
			{ RssMain.dateentree, "08042013" },
			{ RssMain.datesortie, "15122013" } };

	private Object[][][] acte = {
			{ { RssActe.codeccam, "JVJF004 " }, { RssActe.phase, "0" } } };

	private Object[][][] da = { { { RssDa.da, "A280" }, },
			{ { RssDa.da, "Z491" } } };

	public T15Z10E() {
		RssContent rss = buildRssContent(rssmain, acte, da);
		add(rss);
	}

}
