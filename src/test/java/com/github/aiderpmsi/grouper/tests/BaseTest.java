package com.github.aiderpmsi.grouper.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.github.aiderpmsi.pims.grouper.model.RssContent;

public class BaseTest extends RssContent {
	
	protected List<HashMap<String, String>> transform(String[][][] value) {
		List<HashMap<String, String>> returnValue = new ArrayList<>();
		
		for (String[][] value1 : value) {
			HashMap<String, String> map = new HashMap<>();
			for (String[] value2 : value1) {
				map.put(value2[0], value2[1]);
			}
			returnValue.add(map);
		}
		
		return returnValue;
	}
	
}
