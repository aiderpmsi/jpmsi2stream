package com.github.aiderpmsi.pims.parser.linestypes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.aiderpmsi.pims.parser.model.Element;

public class PmsiRegexpElement extends PmsiElementBase {

	private Pattern pattern;
	
	private Matcher matcher = null;
	
	public PmsiRegexpElement(Element config) {
		super(config);
		pattern = Pattern.compile(config.type.substring(7));
		
	}

	@Override
	public boolean validate() {
		
		if (matcher == null) {
			matcher = pattern.matcher(content);
		} else {
			matcher.reset(content);
		}
		
		return matcher.matches();
	}

}
