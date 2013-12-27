package com.github.aiderpmsi.jpmsi2stream.linestypes;

import java.util.regex.Pattern;

/**
 * Définition de la partie Acte d'un RSS 116
 * @author delabre
 *
 */
public class PmsiRss116Acte extends PmsiLineTypeImpl {

	private static final Pattern pattern = Pattern.compile("^(\\d{8})(.{7})(.{1})(.{1})(.{1})(.{4})(.{1})(.{1})(\\d{2})");
	
	private static final String[] names = {
		"DateRealisation", "CodeCCAM", "Phase", "Activite", "ExtensionDoc", "Modificateurs",
		"RemboursementExceptionnel", "AssociationNonPrevue", "NbActes"
	};

	private static final String name = "RssActe";

	public PmsiRss116Acte() {
		super(name, pattern, names, null);
	}
	
}
