package aider.org.pmsi.parser.exceptions;

import aider.org.pmsi.dto.PmsiThreadedPipedReader;
import aider.org.pmsi.dto.PmsiThreadedPipedWriter;

/**
 * Exceptions levées lors de l'erreur d'écriture d'un fichier pmsi sérialisé, ou lors de l'erreur
 * de lecture d'un fichier pmsi sérialisé par pipes ({@link PmsiThreadedPipedWriter} ou {@link PmsiThreadedPipedReader})
 * @author delabre
 *
 */
public class PmsiPipedIOException extends Exception {

	/**
	 * Numéro de série autogénéré
	 */
	private static final long serialVersionUID = -1896810737119225957L;

	
	public PmsiPipedIOException() {
	}

	public PmsiPipedIOException(String arg0) {
		super(arg0);
	}

	public PmsiPipedIOException(Throwable arg0) {
		super(arg0);
	}

	public PmsiPipedIOException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
