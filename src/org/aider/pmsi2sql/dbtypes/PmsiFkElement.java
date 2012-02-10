package org.aider.pmsi2sql.dbtypes;

import java.util.Vector;

import org.apache.commons.lang.StringUtils;

/**
 * Classe définissant une clef étrangère
 * @author delabre
 *
 */
public class PmsiFkElement extends PmsiElement {

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
	 * Constrainte de ce champ
	 */
	private String constrain;
	
	/**
	 * Constructeur
	 * @param myNomChamp String Nom de la clef étrangère
	 * @param myDestination Table de destination de la clef étrangère
	 * @param myConstrain type de contrainte associée (INITIALLY DEFERRED, ...)
	 */
	public PmsiFkElement(String myNomChamp, String myDestination, String myConstrain) {
		super(myNomChamp);
		destination = myDestination;
		destNames = new Vector<String>();
		sourceNames = new Vector<String>();
		constrain = myConstrain;
	}
	
	/**
	 * Rajoute un champ de destination pour la clef étrangère
	 * @param mySourceChamp String Nom du champ source
	 * @param myDestChamp String Nom du champ destination
	 */
	public void addForeignChamp(String mySourceChamp, String myDestChamp) {
		destNames.add(myDestChamp);
		sourceNames.add(mySourceChamp);
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
	
	public String getSQL(String myTableRef) {
		return "ALTER TABLE " + myTableRef + " ADD CONSTRAINT " +
				getNomChamp() + " FOREIGN KEY (" +
				StringUtils.join(sourceNames.iterator(), ", ") + ") REFERENCES " +
				destination + "(" + StringUtils.join(destNames.iterator(), ", ") +
				") " + constrain + ";\n";
	}
}
