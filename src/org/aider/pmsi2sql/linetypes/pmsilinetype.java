package org.aider.pmsi2sql.linetypes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Pattern;

import org.aider.pmsi2sql.dbtypes.pmsidbinternaldbtype;
import org.aider.pmsi2sql.dbtypes.pmsidbtype;
import org.aider.pmsi2sql.dbtypes.pmsifiledbtype;
import org.aider.pmsi2sql.dbtypes.pmsifkdbtype;
import org.aider.pmsi2sql.dbtypes.pmsiindexdbtype;
import org.aider.pmsi2sql.dbtypes.pmsistandarddbtype;
import org.apache.commons.lang.StringUtils;

/**
 * Type g�n�ral de ligne pmsi, permettant d'associer plusieurs {@link pmsidbtype}.
 * Ces diff�rents {@link pmsidbtype} correspondent en fait � 
 * � ins�rer dans une m�me table de base de donn�es
 * @author delabre
 *
 */
public abstract class pmsilinetype {

	/**
	 * Nom de la table de la base de donnn�es
	 */
	private String nom;
	
	/**
	 * Liste des �l�ments de la base de donn�es (champs, index, foreign key)
	 */
	private Vector<pmsidbtype> champs;
	
	/**
	 * Stocke le regex pour pattern matching de mani�re � ne le compiler qu'une fois
	 */
	Pattern regexPattern;
	
	private PreparedStatement InsertSQLStatement;
	
	/**
	 * Constructeur
	 * @param MyNom String Nom de la base de donn�es associ�e
	 */
	public pmsilinetype(String MyNom) {
		champs = new Vector<pmsidbtype>();
		nom = MyNom;
	}
	
	/**
	 * Ajoute un type sql dans la base de donn�es
	 * @param MyChamp {@link pmsidbtype} type sql
	 */
	public void addChamp(pmsidbtype MyChamp) {
		champs.add(MyChamp);
	}
	
	/**
	 * Retourne le nom de la table associ�e � cette ligne
	 * @return String Nom de la table
	 */
	public String getNom() {
		return nom;
	}
	
	/**
	 * Retourne l'instruction SQL n�cessaire pour cr�er les tables
	 */
	public String getSQLTable() {
		Vector<String> MySQLChunks = new Vector<String>();
		
		Enumeration<pmsidbtype> MyEnumTypes = champs.elements();

		while (MyEnumTypes.hasMoreElements()) {
			pmsidbtype MyElt = MyEnumTypes.nextElement();
			if (MyElt instanceof pmsistandarddbtype) {
				pmsistandarddbtype MyNElt = (pmsistandarddbtype) MyElt;
				MySQLChunks.add(MyNElt.getSQL());
			}
		}
		return "CREATE TABLE " + getNom() + " (" +
			StringUtils.join(MySQLChunks.iterator(), ", ")+ ");\n";
	}
	
	/**
	 * Retourne l'instruction sql n�cessaire pour cr�er les index
	 */ 
	public String getSQLIndex() {
		String MyRe = new String();
		
		Enumeration<pmsidbtype> MyEnumTypes = champs.elements();

		while (MyEnumTypes.hasMoreElements()) {
			pmsidbtype MyElt = MyEnumTypes.nextElement();
			if (MyElt instanceof pmsiindexdbtype) {
				pmsiindexdbtype MyNElt = (pmsiindexdbtype) MyElt;
				MyRe = MyRe.concat(MyNElt.getSQL(getNom()));
			}
		}
		return MyRe;
	}
	
	/**
	 * Retourne les instructions n�cessaires pour cr�er les
	 * clefs �trang�res 
	 */
	public String getSQLFK() {
		String MyRe = new String();
		
		Enumeration<pmsidbtype> MyEnumTypes = champs.elements();

		while (MyEnumTypes.hasMoreElements()) {
			pmsidbtype MyElt = MyEnumTypes.nextElement();
			if (MyElt instanceof pmsifkdbtype) {
				pmsifkdbtype MyNElt = (pmsifkdbtype) MyElt;
				MyRe = MyRe.concat(MyNElt.getSQL(getNom()));
			}
		}
		return MyRe;
	}
	
	/**
	 * Retourne la chaine regex pour attraper les infos de la ligne
	 */
	public Pattern getRegex() {
		if (regexPattern != null) {
			return regexPattern;
		} else {
			String MyRe = new String();
		
			Enumeration<pmsidbtype> MyEnumTypes = champs.elements();

			while (MyEnumTypes.hasMoreElements()) {
				pmsidbtype MyElt = MyEnumTypes.nextElement();
				if (MyElt instanceof pmsifiledbtype) {
					pmsifiledbtype MyNElt = (pmsifiledbtype) MyElt;
					MyRe = MyRe.concat(MyNElt.getRegex());
				}
			}
			regexPattern = Pattern.compile("^" + MyRe);
			return regexPattern;
		}
	}
	
	/**
	 * Choisit les valeurs des �l�ments r�cup�r�s dans le fichier lu
	 */
	public void setValues(Vector<String> MyValues) {
		Enumeration<pmsidbtype> MyEnumTypes = champs.elements();
		Iterator<String> MyValue = MyValues.iterator();

		while (MyEnumTypes.hasMoreElements()) {
			pmsidbtype MyElt = MyEnumTypes.nextElement();
			if (MyElt instanceof pmsifiledbtype) {
				MyElt.setValue(MyValue.next());
			}
		}
	}
	
	public String getValue(String MyElementName) {
		Enumeration<pmsidbtype> MyEnumTypes = champs.elements();
		
		while(MyEnumTypes.hasMoreElements()) {
			pmsidbtype MyElt = MyEnumTypes.nextElement();
			if (MyElt.getNomChamp().equals(MyElementName))
				return MyElt.getValue();
		}
		return null;
	}
	
	/**
	 * Ins�re les diff�rentes valeurs contenues dans les {@link pmsifiletype} et les
	 * {@link pmsidbinternaldbtype} dans la table correspondants
	 * @param Myconnection Connection � la base de donn�es
	 * @throws SQLException
	 * @throws FileNotFoundException
	 */
	public void insertSQLLine(Connection Myconnection) throws SQLException {
		Vector<String> MySQLFileTypes = new Vector<String>();
		Vector<String> MySQLFileInterrogations = new Vector<String>();

		Vector<String> MySQLDBTypes = new Vector<String>();
		Vector<String> MySQLDBValues = new Vector<String>();

		Iterator<pmsidbtype> MyDBTypes = champs.iterator();
		
		while (MyDBTypes.hasNext()) {
			pmsidbtype MyElt = MyDBTypes.next();
			if (MyElt instanceof pmsifiledbtype) {
				MySQLFileTypes.add(MyElt.getNomChamp());
				MySQLFileInterrogations.add("?");
			} else if (MyElt instanceof pmsidbinternaldbtype) {
				MySQLDBTypes.add(MyElt.getNomChamp());
				MySQLDBValues.add(MyElt.getValue());
			}
		}
		
		if (InsertSQLStatement == null)
			InsertSQLStatement = Myconnection.prepareStatement("INSERT INTO " + getNom() + "(" +
					 StringUtils.join(MySQLDBTypes.iterator(), ", ") +
						(MySQLDBTypes.size() == 0 || MySQLFileTypes.size() == 0 ? "" : ", ") +
						StringUtils.join(MySQLFileTypes.iterator(), ", ") +
						") VALUES (" +
						StringUtils.join(MySQLDBValues.iterator(), ", ") +
						(MySQLDBTypes.size() == 0 || MySQLFileTypes.size() == 0 ? "" : ", ") +
						StringUtils.join(MySQLFileInterrogations.iterator(), ", ") + ");\n");
		
		MyDBTypes = champs.iterator();
		int MyI = 1;
		while (MyDBTypes.hasNext()) {
			pmsidbtype MyElt = MyDBTypes.next();
			if (MyElt instanceof pmsifiledbtype) {
				pmsifiledbtype MyEltb = (pmsifiledbtype) MyElt;
				if (MyEltb.getValue() == null)
					InsertSQLStatement.setObject(MyI, null);
				else {
					switch (MyEltb.getSqlType()) {
					case BIGINT:
					case INT:
						InsertSQLStatement.setInt(MyI, new Integer(MyEltb.getValue()));
						break;
					case CHAR:
					case VARCHAR:
					case TEXT:
						InsertSQLStatement.setString(MyI, MyEltb.getValue());
						break;
					case DATE:
						InsertSQLStatement.setDate(MyI, java.sql.Date.valueOf(MyEltb.getValue()));
						break;
					case NUMERIC:
						InsertSQLStatement.setBigDecimal(MyI, new BigDecimal(MyEltb.getValue()));
						break;
					case FILE:
						File myFile = new File(MyEltb.getValue());
						FileInputStream myFileIS;
						try {
							myFileIS = new FileInputStream(myFile);
						} catch (FileNotFoundException e) {
							// Il est normalement impossible que le fichier ne soit pas
							// lisible puisque c'est � partir de ce fichier que les donn�es
							// sont lues.
							throw new RuntimeException(e);
						}
						InsertSQLStatement.setBinaryStream(MyI, myFileIS, (int)myFile.length());
						break;
					default:
						throw new RuntimeException("Cas non pr�vu pour l'insertion de donn�es");
					}
				}
				MyI++;
			}
		}
		InsertSQLStatement.execute();
	}
}
