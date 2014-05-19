package com.github.aiderpmsi.pims.grouper.utils;

import com.github.aiderpmsi.pims.grouper.customtags.Group;
import com.github.aiderpmsi.pims.grouper.model.RssContent;

public class Main {

	public static void main(String[] args) throws Exception {
		Grouper gp = new Grouper(); 
		RssContent cont = new T01C041();

		Group group = gp.group(cont);
		if (group == null)
			group = null;
		
	}

}
