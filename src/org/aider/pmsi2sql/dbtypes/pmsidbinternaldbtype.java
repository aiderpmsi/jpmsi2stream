package org.aider.pmsi2sql.dbtypes;

/**
 * Champ interne de la base de donn�es : permet de ne pas le lier avec
 * une donn�e venant du fichier PMSI (SERIAL par exemple)
 * @author delabre
 *
 */
public class pmsidbinternaldbtype extends pmsistandarddbtype {

	/**
	 * Constructeur
	 * @param MyNomChamp  Nom du champ dans la base de donn�es
	 * @param MySQLType Type de donn�e dans la db
	 * @param MySize Taille de la donn�e dans la db
	 * @param myConstrain Constrainte
	 */
	public pmsidbinternaldbtype(String MyNomChamp,
			PmsiStandardDbTypeEnum MySQLType, int MySize, String myConstrain) {
		super(MyNomChamp, MySQLType, MySize);
	}
	
	/**
	 * Constructeur avec absence de contraintes
	 * @param MyNomChamp
	 * @param MySQLType
	 * @param MySize
	 */
	public pmsidbinternaldbtype(String MyNomChamp,
			PmsiStandardDbTypeEnum MySQLType, int MySize) {
		super(MyNomChamp, MySQLType, MySize, "");
	}
}
