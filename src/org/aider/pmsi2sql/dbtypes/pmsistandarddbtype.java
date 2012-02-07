package org.aider.pmsi2sql.dbtypes;

/**
 * Classe définissant un type standard de la base de données.
 * N'est pas utilisée directement
 * @author delabre
 *
 */
public abstract class pmsistandarddbtype extends PmsiElement {

	/**
	 * Type de champ SQL défini par cette classe.
	 * @see PmsiStandardDbTypeEnum
	 */
	private PmsiStandardDbTypeEnum sqlType;

	/**
	 * Taille du champ de la base de données pour les types qui en
	 * définissent une. Dans les autres cas, ce paramètre est inutilisé
	 */
	private int Size;
	
	/**
	 * Constrainte du champ dans la db
	 */
	private String constrain;
	
	/**
	 * Constructeur
	 * @param MyNomChamp String Nom du champ
	 * @param MySQLType Type du champ 
	 * @param MySize int Taille du champ
	 * @param myConstrain Constrainte SQL
	 */
	public pmsistandarddbtype(String MyNomChamp, PmsiStandardDbTypeEnum MySQLType, int MySize, String myConstrain) {
		super(MyNomChamp);
		sqlType = MySQLType;
		Size = MySize;
		constrain = myConstrain;
	}
	
	/**
	 * Constructeur avec absence de contraintes SQL
	 * @param MyNomChamp
	 * @param MySQLType
	 * @param MySize
	 */
	public pmsistandarddbtype(String MyNomChamp, PmsiStandardDbTypeEnum MySQLType, int MySize) {
		super(MyNomChamp);
		sqlType = MySQLType;
		Size = MySize;
		constrain = "";
	}
	
	/**
	 * Renvoie le type de champ de la base de données associé à ce champ
	 * @return {@link PmsiStandardDbTypeEnum} Type de champ
	 */
	public PmsiStandardDbTypeEnum getSqlType() {
		return sqlType;
	}
	
	/**
	 * Renvoie la taille du champ de la base de données
	 * @return <code>int</code> Taille du champ
	 */
	public int getSize() {
		return Size;
	}
	
	/**
	 * Renvoie l'instruction SQL pour créer ce champ
	 * @return {@link String} Instruction SQL
	 */
	public String getSQL() {
		String MyRet = getNomChamp() + " ";
		switch (getSqlType()) {
			case TIMESTAMP:
				MyRet = MyRet.concat("TIMESTAMP WITH TIME ZONE");
				break;
			case TEXT:
				MyRet = MyRet.concat("TEXT");
				break;
			case FILE:
				MyRet = MyRet.concat("BYTEA");
				break;
			case BIGSERIAL:
				MyRet = MyRet.concat("BIGSERIAL");
				break;
			case INT:
				MyRet = MyRet.concat("INT");
				break;
			case BIGINT:
				MyRet = MyRet.concat("BIGINT");
				break;
			case CHAR:
				MyRet = MyRet.concat("CHAR(" + Integer.toString(getSize()) + ")");
				break;
			case DATE:
				MyRet = MyRet.concat("DATE");
				break;
			case NUMERIC:
				MyRet = MyRet.concat("NUMERIC(" + Integer.toString(getSize()) + ")");
				break;
			case VARCHAR:
				MyRet = MyRet.concat("VARCHAR(" + Integer.toString(getSize()) + ")");
				break;
			default:
				break;
			}
			return MyRet + " " + constrain;
		}
	}
