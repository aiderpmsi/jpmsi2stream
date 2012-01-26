package org.aider.pmsi2sql.dbtypes;

import java.util.Vector;

import org.apache.commons.lang.StringUtils;

/**
 * Classe définissant une clef étrangère
 * @author delabre
 *
 */
public class pmsifkdbtype extends pmsidbtype {

	/**
	 * Table de destination
	 */
	private String destination;
	
	/**
	 * Liste des champs dans la table de destination
	 */
	private Vector<String> destNames;
	
	/**
	 * Liste des champs dans la table actuelle
	 */
	private Vector<String> sourceNames;
	
	/**
	 * Constructeur
	 * @param MyNomChamp String Nom de la clef étrangère
	 * @param MyDestination String Destination de la clef étrangère
	 */
	public pmsifkdbtype(String MyNomChamp, String MyDestination) {
		super(MyNomChamp);
		destination = MyDestination;
		destNames = new Vector<String>();
		sourceNames = new Vector<String>();
	}
	
	/**
	 * Rajoute un champ de destination pour la clef étrangère
	 * @param MySourceChamp String Nom du champ source
	 * @param MyDestChamp String Nom du champ destination
	 */
	public void addForeignChamp(String MySourceChamp, String MyDestChamp) {
		destNames.add(MyDestChamp);
		sourceNames.add(MySourceChamp);
	}
	
	/**
	 * Renvoie la table de destination de la clef étrangère
	 * @return String nom de la table
	 */
	public String getDestination() {
		return destination;
	}
	
	/**
	 * Renvoie la liste des champs cibles de la clef étrangère
	 * @return Vector<String> Noms des champs
	 */
	public Vector<String> getDestNames() {
		return destNames;
	}

	/** Renvoie la liste des champs sources de la clef étrangère
	 * @return Vector<String> Noms des champs
	 */
	public Vector<String> getSourceNames() {
		return sourceNames;
	}
	
	public String getSQL(String MyTableRef) {
		return "ALTER TABLE " + MyTableRef + " ADD CONSTRAINT " +
				getNomChamp() + " FOREIGN KEY (" +
				StringUtils.join(sourceNames.iterator(), ", ") + ") REFERENCES " +
				destination + "(" + StringUtils.join(destNames.iterator(), ", ") +
				") DEFERRABLE INITIALLY DEFERRED;\n";
	}
}
