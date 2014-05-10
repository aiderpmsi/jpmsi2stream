package com.github.aiderpmsi.grouper.tests;

public class T15M04E extends BaseTest {

	private String[][] rssmain = {
			{"DP", "Z082    "},
			{"DR", "B202    "},
			{"NbSeances", "0      "},
			{"ModeSortie", "9 "},
			{"DDN", "2013-04-05"},
			{"DateEntree", "2013-04-08"},
			{"DateSortie", "2013-12-15"}
	};
	
	private String[][][] acte = {
			{
				{"CodeCCAM", "JVJF004 "},
				{"Phase", "0"}
			}
	};
	
	private String[][][] da = {
			{
				{"DA", "A280"},
				{"DA", "Z491"}
			}
	};
	
	public T15M04E() {
		setRssacte(transform(acte));
		setRssda(transform(da));
		setRssmain(transform(new String[][][] {rssmain}).get(0));
	}
	
}
