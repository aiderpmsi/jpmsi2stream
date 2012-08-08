package aider.org.pmsi.exceptions;

/**
 * Exception de base des exceptions lancées par le parseur pmsi.
 * @author delabre
 *
 */
// TODO : supprimer le bseoin d'erreur xml et simplifier par la récupération de la
// classe qui a lancé l'erreur
public abstract class PmsiException extends Exception {

	/**
	 * numéro de série autogénéré
	 */
	private static final long serialVersionUID = -336347335654324056L;

	/**
	 * @see Exception#Exception()
	 */
	public PmsiException() {}

	/**
	 * @see Exception#Exception(String)
	 * @param arg0
	 */
	public PmsiException(String arg0) {
		super(arg0);
	}

	/**
	 * @see Exception#Exception(Throwable)
	 * @param arg0
	 */
	public PmsiException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @see Exception#Exception(String, Throwable)
	 * @param arg0
	 * @param arg1
	 */
	public PmsiException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
