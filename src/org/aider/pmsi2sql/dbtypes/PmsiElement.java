package org.aider.pmsi2sql.dbtypes;

/**
 * Classe parente de tous les types de données permettant de faire les liens entre fichier
 * de pmsi et base de données. N'est pas utilisée directement.
 * Une liste de ces classes sont créées dans un objet de type pmsilinetype pour
 * définir le lien entre une ligne pmsi et sa persistance dans la base de données.
 * Un élément peut être :
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
	 * Nom du champ associé dans la base de données
	 */
	private String nomchamp;
	
	/**
	 * Valeur associée à ce champ
	 */
	private String value;

	/**
	 * Constructeur
	 * @param myNomChamp Nom du champ dans la base de données
	 */
	public PmsiElement(String myNomChamp) {
		nomchamp = myNomChamp;
	}
	
	/**
	 * Récupération du nom du champ associé dans la base de données
	 * @return {@link String} nom du champ
	 */
	public String getNomChamp() {
		return nomchamp;
	}
	
	/**
	 * Stocke une valeur dans ce champ
	 * @param myValue Valeur à stocker dans ce champ
	 */
	public void setValue(String myValue) {
		value = myValue;
	}
	
	/**
	 * Retourne la valeur stockée dans ce champ
	 * @return {@link String} Valeur
	 */
	public String getValue() {
		return value;
	}
}
