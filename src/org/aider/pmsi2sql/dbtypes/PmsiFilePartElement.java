package org.aider.pmsi2sql.dbtypes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Cette classe permet de définir un champ de la base de données dont la source
 * provient d'un fichier source pmsi grâce au regex associé
 * @author delabre
 *
 */
public class PmsiFilePartElement extends PmsiStandardElement {

	/**
	 * Chaine de caractère correspondant au regex permettant de capturer
	 * l'expression à rentrer dans la base de données
	 */
	private String regex;
	
	/**
	 * Regex pour les adaptations de date
	 */
	private Pattern datePattern;
	
	/**
	 * Constructeur
	 * @param myNomChamp Nom du champ dans la base de données
	 * @param mySQLType Type de donnée dans la db
	 * @param mySize Taille de la donnée dans la db
	 * @param myConstrain Contrainte du champ SQL
	 * @param myregex Regex capturant la chaine de caractère associée.
	 */
	public PmsiFilePartElement(String myNomChamp, PmsiStandardDbTypeEnum mySQLType,
			int mySize, String myConstrain, String myregex) {
		super(myNomChamp, mySQLType, mySize, myConstrain);
		regex = myregex;
	}

	/**
	 * Constructeur sans contrainte de champ SQL
	 * @param myNomChamp
	 * @param mySQLType
	 * @param mySize
	 * @param myregex
	 */
	public PmsiFilePartElement(String myNomChamp, PmsiStandardDbTypeEnum mySQLType,
			int mySize, String myregex) {
		super(myNomChamp, mySQLType, mySize, "");
		regex = myregex;
	}

	/**
	 * Renvoie le regex du champ
	 * @return {@link String} Regex
	 */
	public String getRegex() {
		return regex;
	}
	
	/**
	 * Définit la valeur associée au champ. Elle est modifiée selon le type de
	 * valeur que le champ doit contenir :
	 * <ul>
	 * <li> VARCHAR et CHAR : stockés tels quels</li>
	 * <li> DATE : transformée de DDMMYYYY en YYYY-MM-DD ou NULL si valeur ne correspondant pas à DDMMYYYY</li>
	 * <li> Autres : les espaces sont enleves avant et après, et si il ne reste rien, transformés en null 
	 * </ul>
	 * @param myValue Valeur à associer à ce champ
	 */
	public void setValue(String myValue) {
		// Suppression des espaces si on a une valeur non textuelle
		switch (getSqlType()) {
		case VARCHAR:
		case CHAR:
		case TEXT:
		case FILE:
			break;
		default:
			myValue = myValue.trim();
		}
		// Une valeur non textuelle vide est transformée en NULL
		switch(getSqlType()) {
		case VARCHAR:
		case CHAR:
		case TEXT:
		case FILE:
			break;
		case DATE:
			if (datePattern == null)
				datePattern = Pattern.compile("^(\\d{2})(\\d{2})(\\d{4})");
			Matcher m = datePattern.matcher(myValue);
			if (m.matches()) {
				myValue = m.group(3) + "-" + m.group(2) + "-" + m.group(1);
			}
		default:
			if (myValue.length() == 0)
				myValue = null;
		}
		super.setValue(myValue);
	}
	
}
