package aider.org.pmsi.parser.exceptions;

import aider.org.pmsi.writer.PmsiWriter;

/**
 * Exceptions levées lors de l'erreur d'écriture d'un fichier pmsi sérialisé, ou lors de l'erreur
 * de lecture d'un fichier pmsi sérialisé par pipes ({@link PmsiWriter} ou {@link PmsiThreadedPipedReader})
 * @author delabre
 *
 */
public class PmsiIOWriterException extends Exception {

	/**
	 * Numéro de série autogénéré
	 */
	private static final long serialVersionUID = -1896810737119225957L;

	
	public PmsiIOWriterException() {
	}

	public PmsiIOWriterException(String arg0) {
		super(arg0);
	}

	public PmsiIOWriterException(Throwable arg0) {
		super(arg0);
	}

	public PmsiIOWriterException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
