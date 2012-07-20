package aider.org.pmsi.parser.exceptions;

import aider.org.pmsi.writer.PmsiWriter;

/**
 * Exceptions levées lors de l'erreur d'écriture d'un fichier pmsi sérialisé, ou lors de l'erreur
 * de lecture d'un fichier pmsi sérialisé par pipes ({@link PmsiWriter} ou {@link PmsiThreadedPipedReader})
 * @author delabre
 *
 */
public class PmsiWriterException extends PmsiException {

	/**
	 * Numéro de série autogénéré
	 */
	private static final long serialVersionUID = -1896810737119225957L;

	
	public PmsiWriterException() {
	}

	public PmsiWriterException(String arg0) {
		super(arg0);
	}

	public PmsiWriterException(Throwable arg0) {
		super(arg0);
	}

	public PmsiWriterException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
