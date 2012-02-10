package org.aider.pmsi2sql.dbtypes;

/**
 * Champ interne de la base de donn�es : permet de ne pas le lier avec
 * une donn�e venant du fichier PMSI (SERIAL par exemple)
 * @author delabre
 *
 */
public class PmsiInternalElement extends PmsiStandardElement {

	/**
	 * Constructeur
	 * @param myNomChamp  Nom du champ dans la base de donn�es
	 * @param mySQLType Type de donn�e dans la db
	 * @param mySize Taille de la donn�e dans la db
	 * @param myConstrain Constrainte
	 */
	public PmsiInternalElement(String myNomChamp,
			PmsiStandardDbTypeEnum mySQLType, int mySize, String myConstrain) {
		super(myNomChamp, mySQLType, mySize);
	}
	
	/**
	 * Constructeur avec absence de contraintes
	 * @param myNomChamp
	 * @param mySQLType
	 * @param mySize
	 */
	public PmsiInternalElement(String myNomChamp,
			PmsiStandardDbTypeEnum mySQLType, int mySize) {
		super(myNomChamp, mySQLType, mySize, "");
	}
}
