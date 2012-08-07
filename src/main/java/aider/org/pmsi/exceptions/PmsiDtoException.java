package aider.org.pmsi.exceptions;

import aider.org.pmsi.dto.PmsiDto;

/**
 * Exceptions levées lors d'une erreur lors du transfert des données
 * dans le support de sérialisation
 * @author delabre
 * @see PmsiDto
 */
public class PmsiDtoException extends PmsiException {

	/**
	 * Numéro de série autogénéré
	 */
	private static final long serialVersionUID = 8837938697462130158L;

	/**
	 * @see PmsiException#PmsiException()
	 */	
	public PmsiDtoException() {
	}

	/**
	 * @see PmsiException#PmsiException(String)
	 * @param arg0
	 */
	public PmsiDtoException(String arg0) {
		super(arg0);
	}

	/**
	 * @see PmsiException#PmsiException(Throwable)
	 * @param arg0
	 */
	public PmsiDtoException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @see PmsiException#PmsiException(String, Throwable)
	 * @param arg0
	 * @param arg1
	 */
	public PmsiDtoException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
