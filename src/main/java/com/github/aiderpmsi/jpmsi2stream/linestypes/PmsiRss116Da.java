package com.github.aiderpmsi.jpmsi2stream.linestypes;

import java.util.regex.Pattern;

/**
 * Définition de la partie Diagnostics associés d'un RSS 116
 * @author delabre
 *
 */
public class PmsiRss116Da extends PmsiLineTypeImpl {

	private static final Pattern pattern = Pattern.compile("^(.{8})");
	
	private static final String[] names = {
		"DA"
	};
	
	private static final String name = "RssDa";
	
	public PmsiRss116Da() {
		super(name, pattern, names, null);
	}
	
}
