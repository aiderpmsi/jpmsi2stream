package org.aider.pmsi2sql.dbtypes;

import java.util.Vector;

import org.apache.commons.lang.StringUtils;

/***
 * Classe définissant un index dans la base de données
 * @author delabre
 *
 */
public class PmsiIndexElement extends PmsiElement {

	public static final int INDEX_SIMPLE = 0;
	public static final int INDEX_PK = 1;
	public static final int INDEX_UNIQUE = 2;
	/**
	 * Type d'index
	 * @see INDEX_SIMPLE
	 * @see INDEX_PK
	 * @see INDEX_UNIQUE
	 */
	private int indextype;
	
	/**
	 * Liste des noms des champs de l'index créé
	 */
	private Vector<String> indexName;
	
	/**
	 * Constructeur
	 * @param MyNomChamp String Nom du champ créé
	 * @param MyIndexType int Type d'index (voir {@link indextype})
	 */
	public PmsiIndexElement(String MyNomChamp,
			int MyIndexType) {
		super(MyNomChamp);
		indextype = MyIndexType;
		indexName = new Vector<String>();
	}
	
	/**
	 * Ajout d'un nom de champ dans l'index
	 * @param MyIndexName String Champ à indexer
	 */
	public void addIndex(String MyIndexName) {
		indexName.add(MyIndexName);
	}
	
	/**
	 * Récupération du type d'index créé
	 * @return int (voir {@link indextype})
	 */
	public int getIndexType() {
		return indextype;
	}
	
	/**
	 * Récupération de la liste des champs d'index
	 * @return Vector<String> Liste des champs indexés
	 */
	public Vector<String> getIndexName() {
		return indexName;
	}
	
	/**
	 * Renvoie l'instruction pour créer l'index
	 * @return
	 */
	public String getSQL(String MyTableRef) {
		switch(getIndexType()) {
		case INDEX_PK:
			return "ALTER TABLE " + MyTableRef + " ADD PRIMARY KEY (" +
				StringUtils.join(indexName.iterator(), ", ") + ");\n";
		case INDEX_SIMPLE:
			return "CREATE INDEX " + getNomChamp() + " ON " + MyTableRef +
					" USING btree (" + StringUtils.join(indexName.iterator(), ", ") + ");\n";
		case INDEX_UNIQUE:			
			return "CREATE UNIQUE INDEX " + getNomChamp() + " ON " + MyTableRef +
					" USING btree (" + StringUtils.join(indexName.iterator(), ", ") + ");\n";
		default:
			return "";
		}
	}
}
