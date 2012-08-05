package aider.org.pmsi.exceptions;

import java.io.OutputStream;

import aider.org.pmsi.dto.PmsiStreamMuxer;
import aider.org.pmsi.writer.PmsiWriter;

/**
 * Exceptions levées lors de l'erreur d'écriture d'un fichier pmsi sérialisé
 * dans le {@link OutputStream} de la classe {@link PmsiStreamMuxer} par {@link PmsiWriter}
 * @author delabre
 * @see PmsiWriter
 */
public class PmsiWriterException extends PmsiException {

	/**
	 * Numéro de série autogénéré
	 */
	private static final long serialVersionUID = -1896810737119225957L;

	
	/**
	 * @see PmsiException#PmsiException()
	 */	
	public PmsiWriterException() {
	}

	/**
	 * @see PmsiException#PmsiException(String)
	 * @param arg0
	 */
	public PmsiWriterException(String arg0) {
		super(arg0);
	}

	/**
	 * @see PmsiException#PmsiException(Throwable)
	 * @param arg0
	 */
	public PmsiWriterException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @see PmsiException#PmsiException(String, Throwable)
	 * @param arg0
	 * @param arg1
	 */
	public PmsiWriterException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
