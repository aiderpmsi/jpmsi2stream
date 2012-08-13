package aider.org.pmsi.parser.linestypes;

import java.util.regex.Pattern;

/**
 * Définition de la partie diagnostics documentaires d'un RSS 116
 * @author delabre
 *
 */
public class PmsiRss116Dad extends PmsiLineTypeImpl {

	private static final Pattern pattern = Pattern.compile("^(.{8})");
	
	private static final String[] names = {
		"DAD"
	};
	
	private static final String name = "RssDad";

	public PmsiRss116Dad() {
		super(name, pattern, names, null);
	}
	
}
