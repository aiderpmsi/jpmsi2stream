package aider.org.pmsi.exceptions;

/**
 * Exception de base des exceptions lancées par le parseur pmsi.
 * Elle permet de définir :
 * <ul>
 *  <li>Le message d'erreur comme d'habitude</li>
 *  <li>Un message d'erreur xml pour simplifier le report de réussite
 *    ou d'échec de parsing</li>
 * </ul>
 * @author delabre
 *
 */
// TODO : supprimer le bseoin d'erreur xml et simplifier par la récupération de la
// classe qui a lancé l'erreur
public abstract class PmsiException extends Exception {

	/**
	 * Message : chaine vide par défaut
	 */
	private String xmlMessage = "";
	
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

	/**
	 * Définit le message xml de cette exception
	 * @return message xml
	 */
	public String getXmlMessage() {
		return xmlMessage;
	}

	/**
	 * Retourne le message xml de cette exception
	 * @param xmlMessage
	 * @return l'exception {@link PmsiException} actuelle (pour faire des set en cascade)
	 */
	public PmsiException setXmlMessage(String xmlMessage) {
		this.xmlMessage = xmlMessage;
		return this;
	}

}
