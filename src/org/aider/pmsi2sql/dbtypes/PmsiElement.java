package org.aider.pmsi2sql.dbtypes;

/**
 * Classe parente de tous les types de donn�es permettant de faire les liens entre fichier
 * de pmsi et base de donn�es. N'est pas utilis�e directement.
 * Une liste de ces classes sont cr��es dans un objet de type pmsilinetype pour
 * d�finir le lien entre une ligne pmsi et sa persistance dans la base de donn�es.
 * Un �l�ment peut �tre :
 * <ul>
 * <li>Un champ à lire dans un fichier</li>
 * <li>Un compteur (ligne, ...) avec un compteur</li>
 * <li>Un index</li>
 * <li>Une clef étrangère</li>
 * </ul> 
 * @author delabre
 */
public abstract class PmsiElement {
	
	/**
	 * Nom du champ associé dans la base de donn�es
	 */
	private String nomchamp;
	
	/**
	 * Valeur associ�e � ce champ
	 */
	private String value;

	/**
	 * Constructeur
	 * @param myNomChamp Nom du champ dans la base de donn�es
	 */
	public PmsiElement(String myNomChamp) {
		nomchamp = myNomChamp;
	}
	
	/**
	 * R�cup�ration du nom du champ associ� dans la base de donn�es
	 * @return {@link String} nom du champ
	 */
	public String getNomChamp() {
		return nomchamp;
	}
	
	/**
	 * Stocke une valeur dans ce champ
	 * @param myValue Valeur � stocker dans ce champ
	 */
	public void setValue(String myValue) {
		value = myValue;
	}
	
	/**
	 * Retourne la valeur stock�e dans ce champ
	 * @return {@link String} Valeur
	 */
	public String getValue() {
		return value;
	}
}
