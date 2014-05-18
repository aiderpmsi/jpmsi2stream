package com.github.aiderpmsi.grouper.tests;

public class T08M231 extends BaseTest {

	private String[][] rssmain = {
			{"DP", "S9241    "},
			{"DR", "B202    "},
			{"NbSeances", "0      "},
			{"ModeSortie", "9 "},
			{"DDN", "1970-04-05"},
			{"DateEntree", "2013-04-08"},
			{"DateSortie", "2013-04-14"}
	};
	
	private String[][][] acte = {
			{
				{"CodeCCAM", "JVJF004 "},
				{"Phase", "0"}
			},
			{
				{"CodeCCAM", "AAJA001 "},
				{"Phase", "0"}
			}
	};
	
	private String[][][] da = {
			{
				{"DA", "A280"},
			},
			{
				{"DA", "Z491"}
			}
	};
	
	public T08M231() {
		setRssacte(transform(acte));
		setRssda(transform(da));
		setRssmain(transform(new String[][][] {rssmain}).get(0));
	}
	
}
