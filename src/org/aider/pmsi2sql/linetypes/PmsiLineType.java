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

import org.aider.pmsi2sql.dbtypes.PmsiInternalElement;
import org.aider.pmsi2sql.dbtypes.PmsiElement;
import org.aider.pmsi2sql.dbtypes.PmsiFilePartElement;
import org.aider.pmsi2sql.dbtypes.PmsiFkElement;
import org.aider.pmsi2sql.dbtypes.PmsiIndexElement;
import org.aider.pmsi2sql.dbtypes.PmsiStandardElement;
import org.apache.commons.lang.StringUtils;

/**
 * D�fini l'architecture pour cr�er des patrons de lignes pmsi en associant des
 * {@link PmsiElement} � la suite, le tout d�finissant une suite de caract�res � attrapper
 * permettant au final de lire des lignes ou des parties de lignes des fichiers pmsi.
 * @author delabre
 *
 */
public abstract class PmsiLineType {

	/**
	 * Nom de la table de la base de donnn�es
	 */
	private String nom;
	
	/**
	 * Liste des �l�ments de la base de donn�es (champs, index, foreign key)
	 */
	private Vector<PmsiElement> champs;
	
	/**
	 * Stocke le regex pour pattern matching de mani�re � ne le compiler qu'une fois
	 */
	Pattern regexPattern;
	
	private PreparedStatement InsertSQLStatement;
	
	/**
	 * Constructeur
	 * @param myNom String Nom de la base de donn�es associ�e
	 */
	public PmsiLineType(String myNom) {
		champs = new Vector<PmsiElement>();
		nom = myNom;
	}
	
	/**
	 * Ajoute un type sql dans la base de donn�es
	 * @param MyChamp {@link PmsiElement} type sql
	 */
	public void addChamp(PmsiElement myChamp) {
		champs.add(myChamp);
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
	 * @return String Instruction SQL n�cessaire pour cr�er la table, sans liens ni index.
	 */
	public String getSQLTable() {
		Vector<String> MySQLChunks = new Vector<String>();
		
		Enumeration<PmsiElement> MyEnumTypes = champs.elements();

		while (MyEnumTypes.hasMoreElements()) {
			PmsiElement MyElt = MyEnumTypes.nextElement();
			if (MyElt instanceof PmsiStandardElement) {
				PmsiStandardElement MyNElt = (PmsiStandardElement) MyElt;
				MySQLChunks.add(MyNElt.getSQL());
			}
		}
		return "CREATE TABLE " + getNom() + " (" +
			StringUtils.join(MySQLChunks.iterator(), ", ")+ ");\n";
	}
	
	/**
	 * Retourne l'instruction sql n�cessaire pour cr�er les index
	 * @return String Instruction SQL n�cessaire pour cr�er les index dans la table 
	 */ 
	public String getSQLIndex() {
		String MyRe = new String();
		
		Enumeration<PmsiElement> MyEnumTypes = champs.elements();

		while (MyEnumTypes.hasMoreElements()) {
			PmsiElement MyElt = MyEnumTypes.nextElement();
			if (MyElt instanceof PmsiIndexElement) {
				PmsiIndexElement MyNElt = (PmsiIndexElement) MyElt;
				MyRe = MyRe.concat(MyNElt.getSQL(getNom()));
			}
		}
		return MyRe;
	}
	
	/**
	 * Retourne les instructions n�cessaires pour cr�er les
	 * clefs �trang�res 
	 * @return {@link String} Instruction SQL n�cessaire pour cr�er les clefs �trang�res.
	 */
	public String getSQLFK() {
		String MyRe = new String();
		
		Enumeration<PmsiElement> MyEnumTypes = champs.elements();

		while (MyEnumTypes.hasMoreElements()) {
			PmsiElement MyElt = MyEnumTypes.nextElement();
			if (MyElt instanceof PmsiFkElement) {
				PmsiFkElement MyNElt = (PmsiFkElement) MyElt;
				MyRe = MyRe.concat(MyNElt.getSQL(getNom()));
			}
		}
		return MyRe;
	}
	
	/**
	 * Retourne la chaine regex pour attraper les infos de la ligne
	 * @return {@link Pattern} Regex permettant d'attraper la ligne ou la partie de ligne correspondant � ce patron 
	 */
	public Pattern getRegex() {
		if (regexPattern != null) {
			return regexPattern;
		} else {
			String MyRe = new String();
		
			Enumeration<PmsiElement> MyEnumTypes = champs.elements();

			while (MyEnumTypes.hasMoreElements()) {
				PmsiElement MyElt = MyEnumTypes.nextElement();
				if (MyElt instanceof PmsiFilePartElement) {
					PmsiFilePartElement MyNElt = (PmsiFilePartElement) MyElt;
					MyRe = MyRe.concat(MyNElt.GetRegex());
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
		Enumeration<PmsiElement> MyEnumTypes = champs.elements();
		Iterator<String> MyValue = MyValues.iterator();

		while (MyEnumTypes.hasMoreElements()) {
			PmsiElement MyElt = MyEnumTypes.nextElement();
			if (MyElt instanceof PmsiFilePartElement) {
				MyElt.setValue(MyValue.next());
			}
		}
	}
	
	public String getValue(String MyElementName) {
		Enumeration<PmsiElement> MyEnumTypes = champs.elements();
		
		while(MyEnumTypes.hasMoreElements()) {
			PmsiElement MyElt = MyEnumTypes.nextElement();
			if (MyElt.getNomChamp().equals(MyElementName))
				return MyElt.getValue();
		}
		return null;
	}
	
	/**
	 * Ins�re les diff�rentes valeurs contenues dans les {@link pmsifiletype} et les
	 * {@link PmsiInternalElement} dans la table correspondants
	 * @param Myconnection Connection � la base de donn�es
	 * @throws SQLException
	 * @throws FileNotFoundException
	 */
	public void insertSQLLine(Connection Myconnection) throws SQLException {
		Vector<String> MySQLFileTypes = new Vector<String>();
		Vector<String> MySQLFileInterrogations = new Vector<String>();

		Vector<String> MySQLDBTypes = new Vector<String>();
		Vector<String> MySQLDBValues = new Vector<String>();

		Iterator<PmsiElement> MyDBTypes = champs.iterator();
		
		while (MyDBTypes.hasNext()) {
			PmsiElement MyElt = MyDBTypes.next();
			if (MyElt instanceof PmsiFilePartElement) {
				MySQLFileTypes.add(MyElt.getNomChamp());
				MySQLFileInterrogations.add("?");
			} else if (MyElt instanceof PmsiInternalElement) {
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
			PmsiElement MyElt = MyDBTypes.next();
			if (MyElt instanceof PmsiFilePartElement) {
				PmsiFilePartElement MyEltb = (PmsiFilePartElement) MyElt;
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
