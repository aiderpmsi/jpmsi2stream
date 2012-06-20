package aider.org.pmsi.parser.exceptions;

import java.util.concurrent.ExecutionException;

/**
 * Classe d'exception indiquant une impossibilit� � lire le
 * fichier PMSI. Ceci indique que le fichier n'est pas valide dans la forme (contenant),
 * probablement parceque ce lecteur ne prend pas en charge ce format.
 * @author delabre
 *
 */
public class PmsiFileNotReadable extends ExecutionException {

	/**
	 * Num�ro de s�rie
	 */
	private static final long serialVersionUID = -5315118463220885515L;

	/**
	 * Constructeur
	 * @param myReason
	 */
	public PmsiFileNotReadable(String myReason) {
		super(myReason);
	}
	
	/**
	 * Constructeur
	 * @param myReason
	 * @param myParentException
	 */
	public PmsiFileNotReadable(String myReason, Exception myParentException) {
		super(myReason.concat((myParentException.getMessage() == null ? "" : myParentException.getMessage())), myParentException);
	}
}
