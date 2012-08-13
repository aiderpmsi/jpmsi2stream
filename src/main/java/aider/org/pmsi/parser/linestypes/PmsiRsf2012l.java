package aider.org.pmsi.parser.linestypes;

import java.util.regex.Pattern;

/**
 * Définition d'une ligne L de RSF version 2012
 * @author delabre
 *
 */
public class PmsiRsf2012l extends PmsiLineTypeImpl {

	private static final Pattern pattern = Pattern.compile("^(L)(\\d{9})(.{20})(.{13})(.{2})(.{3})(.{9})(.{2})(.{3})(.{8})(.{2})(.{8})" +
			"(.{8})(.{2})(.{8})(.{8})(.{2})(.{8})(.{8})(.{2})(.{8})(.{8})(.{2})(.{8})");
	
	private static final String[] names = {
		"TypeEnregistrement", "Finess", "NumRSS", "CodeSS", "CleCodeSS", "RangBeneficiaire", "NumFacture",
		"ModeTraitement", "Discipline", "DateActe1", "QuantiteActe1", "CodeActe1", "DateActe2",
		"QuantiteActe2", "CodeActe2", "DateActe3", "QuantiteActe3", "CodeActe3", "DateActe4",
		"QuantiteActe4", "CodeActe4", "DateActe5", "QuantiteActe5", "CodeActe5"
	};

	private static final String name = "RsfL";

	public PmsiRsf2012l() {
		super(name, pattern, names, null);
	}
	
}
