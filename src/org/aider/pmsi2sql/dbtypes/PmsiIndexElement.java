package org.aider.pmsi2sql.dbtypes;

import java.util.Vector;

import org.apache.commons.lang.StringUtils;

/***
 * Classe d�finissant un index dans la base de donn�es
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
	 * Liste des noms des champs de l'index cr��
	 */
	private Vector<String> indexName;
	
	/**
	 * Constructeur
	 * @param MyNomChamp String Nom du champ cr��
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
	 * @param MyIndexName String Champ � indexer
	 */
	public void addIndex(String MyIndexName) {
		indexName.add(MyIndexName);
	}
	
	/**
	 * R�cup�ration du type d'index cr��
	 * @return int (voir {@link indextype})
	 */
	public int getIndexType() {
		return indextype;
	}
	
	/**
	 * R�cup�ration de la liste des champs d'index
	 * @return Vector<String> Liste des champs index�s
	 */
	public Vector<String> getIndexName() {
		return indexName;
	}
	
	/**
	 * Renvoie l'instruction pour cr�er l'index
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
