package aider.org.pmsi.parser.exceptions;

import java.util.concurrent.ExecutionException;

/**
 * Classe d'exception indiquant une impossibilité à rendre persistant le
 * fichier PMSI. Ceci indique que le fichier n'est pas valide dans le fond (contenu)
 * @author delabre
 *
 */
public class PmsiFileNotInserable extends ExecutionException {

	/**
	 * Numéro de série 
	 */
	private static final long serialVersionUID = 7233844278355609158L;

	/**
	 * Constructeur
	 * @param myReason
	 */
	public PmsiFileNotInserable(String myReason) {
		super(myReason);
	}
	
	/**
	 * Constructeur
	 * @param myReason
	 * @param myParentException
	 */
	public PmsiFileNotInserable(String myReason, Exception myParentException) {
		super(myReason.concat((myParentException.getMessage() == null ? "" : myParentException.getMessage())), myParentException);
	}
}
