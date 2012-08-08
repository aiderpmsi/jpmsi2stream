package aider.org.pmsi.exceptions;

import aider.org.pmsi.parser.PmsiParser;

/**
 * Exception levée lors d'une erreur de lecture d'un fichier de pmsi
 * @author delabre
 * @see PmsiParser
 */
public class PmsiParserException extends PmsiException {

	/**
	 * Numéro de série autogénéré
	 */
	private static final long serialVersionUID = -5876923561804105709L;

	/**
	 * @see PmsiException#PmsiException()
	 */	
	public PmsiParserException() {
	}

	/**
	 * @see PmsiException#PmsiException(String)
	 * @param arg0
	 */
	public PmsiParserException(String arg0) {
		super(arg0);
	}

	/**
	 * @see PmsiException#PmsiException(Throwable)
	 * @param arg0
	 */
	public PmsiParserException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @see PmsiException#PmsiException(String, Throwable)
	 * @param arg0
	 * @param arg1
	 */
	public PmsiParserException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
