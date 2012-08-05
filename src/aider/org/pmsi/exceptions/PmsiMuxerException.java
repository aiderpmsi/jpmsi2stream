package aider.org.pmsi.exceptions;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import aider.org.pmsi.dto.PmsiStreamMuxer;

/**
 * Exceptions levées lors d'une erreur au cours du transfert des données
 * entre {@link PipedInputStream} et {@link PipedOutputStream} de la classe
 * {@link PmsiStreamMuxer}
 * dans le support de sérialisation
 * @author delabre
 * @see PmsiStreamMuxer
 */
public class PmsiMuxerException extends PmsiException {

	/**
	 * Numéro de série autogénéré
	 */
	private static final long serialVersionUID = 4661678139228613298L;

	/**
	 * @see PmsiException#PmsiException()
	 */
	public PmsiMuxerException() {
	}

	/**
	 * @see PmsiException#PmsiException(String)
	 * @param arg0
	 */
	public PmsiMuxerException(String arg0) {
		super(arg0);
	}

	/**
	 * @see PmsiException#PmsiException(Throwable)
	 * @param arg0
	 */
	public PmsiMuxerException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @see PmsiException#PmsiException(String, Throwable)
	 * @param arg0
	 * @param arg1
	 */
	public PmsiMuxerException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
