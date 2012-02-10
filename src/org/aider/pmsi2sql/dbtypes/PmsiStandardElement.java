package org.aider.pmsi2sql.dbtypes;

/**
 * Classe d�finissant un type standard de la base de donn�es.
 * N'est pas utilis�e directement
 * @author delabre
 *
 */
public abstract class PmsiStandardElement extends PmsiElement {

	/**
	 * Type de champ SQL d�fini par cette classe.
	 * @see PmsiStandardDbTypeEnum
	 */
	private PmsiStandardDbTypeEnum sqlType;

	/**
	 * Taille du champ de la base de donn�es pour les types qui en
	 * d�finissent une. Dans les autres cas, ce param�tre est inutilis�
	 */
	private int Size;
	
	/**
	 * Constrainte du champ dans la db
	 */
	private String constrain;
	
	/**
	 * Constructeur
	 * @param myNomChamp String Nom du champ
	 * @param mySQLType Type du champ 
	 * @param mySize int Taille du champ
	 * @param myConstrain Constrainte SQL
	 */
	public PmsiStandardElement(String myNomChamp, PmsiStandardDbTypeEnum mySQLType, int mySize, String myConstrain) {
		super(myNomChamp);
		sqlType = mySQLType;
		Size = mySize;
		constrain = myConstrain;
	}
	
	/**
	 * Constructeur avec absence de contraintes SQL
	 * @param myNomChamp
	 * @param mySQLType
	 * @param mySize
	 */
	public PmsiStandardElement(String myNomChamp, PmsiStandardDbTypeEnum mySQLType, int mySize) {
		super(myNomChamp);
		sqlType = mySQLType;
		Size = mySize;
		constrain = "";
	}
	
	/**
	 * Renvoie le type de champ de la base de donn�es associ� � ce champ
	 * @return {@link PmsiStandardDbTypeEnum} Type de champ
	 */
	public PmsiStandardDbTypeEnum getSqlType() {
		return sqlType;
	}
	
	/**
	 * Renvoie la taille du champ de la base de donn�es
	 * @return <code>int</code> Taille du champ
	 */
	public int getSize() {
		return Size;
	}
	
	/**
	 * Renvoie l'instruction SQL pour cr�er ce champ
	 * @return {@link String} Instruction SQL
	 */
	public String getSQL() {
		String myRet = getNomChamp() + " ";
		switch (getSqlType()) {
			case TIMESTAMP:
				myRet = myRet.concat("TIMESTAMP WITH TIME ZONE");
				break;
			case TEXT:
				myRet = myRet.concat("TEXT");
				break;
			case FILE:
				myRet = myRet.concat("BYTEA");
				break;
			case BIGSERIAL:
				myRet = myRet.concat("BIGSERIAL");
				break;
			case INT:
				myRet = myRet.concat("INT");
				break;
			case BIGINT:
				myRet = myRet.concat("BIGINT");
				break;
			case CHAR:
				myRet = myRet.concat("CHAR(" + Integer.toString(getSize()) + ")");
				break;
			case DATE:
				myRet = myRet.concat("DATE");
				break;
			case NUMERIC:
				myRet = myRet.concat("NUMERIC(" + Integer.toString(getSize()) + ")");
				break;
			case VARCHAR:
				myRet = myRet.concat("VARCHAR(" + Integer.toString(getSize()) + ")");
				break;
			default:
				break;
			}
			return myRet + " " + constrain;
		}
	}
