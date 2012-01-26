package org.aider.pmsi2sql.dbtypes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Cette classe permet de définir un champ de la base de données dont la source
 * provient d'un fichier source pmsi grâce au regex associé
 * 
 * @author delabre
 *
 */
public class pmsifiledbtype extends pmsistandarddbtype {

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
	 * @param MyNomChamp Nom du champ dans la base de données
	 * @param MySQLType Type de donnée dans la db
	 * @param MySize Taille de la donnée dans la db
	 * @param myConstrain Contrainte du champ SQL
	 * @param Myregex Regex capturant la chaine de caractère associée.
	 */
	public pmsifiledbtype(String MyNomChamp, PmsiStandardDbTypeEnum MySQLType,
			int MySize, String myConstrain, String Myregex) {
		super(MyNomChamp, MySQLType, MySize, myConstrain);
		regex = Myregex;
	}

	/**
	 * Constructeur sans contrainte de champ SQL
	 * @param MyNomChamp
	 * @param MySQLType
	 * @param MySize
	 * @param Myregex
	 */
	public pmsifiledbtype(String MyNomChamp, PmsiStandardDbTypeEnum MySQLType,
			int MySize, String Myregex) {
		super(MyNomChamp, MySQLType, MySize, "");
		regex = Myregex;
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
	 * @param MyValue Valeur à associer à ce champ
	 */
	public void setValue(String MyValue) {
		// Suppression des espaces si on a une valeur non textuelle
		switch (getSqlType()) {
		case VARCHAR:
		case CHAR:
		case TEXT:
		case FILE:
			break;
		default:
			MyValue = MyValue.trim();
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
			Matcher m = datePattern.matcher(MyValue);
			if (m.matches()) {
				MyValue = m.group(3) + "-" + m.group(2) + "-" + m.group(1);
			}
		default:
			if (MyValue.length() == 0)
				MyValue = null;
		}
		super.setValue(MyValue);
	}
	
}
