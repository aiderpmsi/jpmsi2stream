package com.github.aiderpmsi.pims.parser.linestypes.elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.aiderpmsi.pims.parser.model.PmsiElementConfig;

public class PmsiRegexpElement extends PmsiElementBase {

	private Pattern pattern;
	
	private Matcher matcher = null;
	
	public PmsiRegexpElement(PmsiElementConfig config) {
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
