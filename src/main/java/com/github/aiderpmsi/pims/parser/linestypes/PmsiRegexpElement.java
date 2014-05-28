package com.github.aiderpmsi.pims.parser.linestypes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.Segment;

import com.github.aiderpmsi.pims.parser.model.Element;

public class PmsiRegexpElement extends PmsiElementBase {

	private Pattern pattern;
	
	private Matcher matcher = null;
	
	public PmsiRegexpElement(Element config) {
		super(config);
		pattern = Pattern.compile(config.type.substring(7));
		
	}

	@Override
	public boolean parse(Segment segt) {
		this.content = segt;
		if (matcher == null) {
			matcher = pattern.matcher(segt);
		} else {
			matcher.reset(segt);
		}
		
		return matcher.matches();
	}

}
