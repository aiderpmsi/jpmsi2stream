package aider.org.pmsi.parser.exceptions;

import aider.org.pmsi.parser.PmsiReader;

/**
 * Exception levée lors d'une erreur de lecture d'un fichier de pmsi (dans {@link PmsiReader} et
 * les classes dérivées
 * @author delabre
 *
 */
public class PmsiReaderException extends PmsiException {

	/**
	 * Numéro de série autogénéré
	 */
	private static final long serialVersionUID = -5876923561804105709L;

	public PmsiReaderException() {
	}

	public PmsiReaderException(String arg0) {
		super(arg0);
	}

	public PmsiReaderException(Throwable arg0) {
		super(arg0);
	}

	public PmsiReaderException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
