package org.aider.pmsi2sql;

import java.io.FileReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.aider.pmsi2sql.linetypes.PmsiInsertion;
import org.aider.pmsi2sql.linetypes.PmsiInsertionResult;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import aider.org.pmsi.parser.PmsiReader;
import aider.org.pmsi.parser.exceptions.PmsiFileNotInserable;
import aider.org.pmsi.parser.exceptions.PmsiFileNotReadable;
import aider.org.pmsi.reader.PmsiRSF2009Reader;
import aider.org.pmsi.reader.PmsiRSS116Reader;

/**
 * Entr�e du programme permettant d'ins�rer un fichier pmsi dans la base de donn�es ad�quate. Contient
 * la fonction main de pmsi2sql
 * @author delabre 
 *
 */
public class Pmsi2Sql {

	/**
	 * Enum�ration permettant d'indiquer quel lecteur a r�ussi � r�aliser la lecture du fichier
	 * pmis (et donc de quel type de format le fichier est
	 * @author delabre
	 *
	 */
	public enum FileType {
		FALSE, RSS, RSF, RSF2012;
	}
	
	/**
	 * Chaine de caract�re stockant les erreurs des
	 * lecteurs de fichiers de PMSI
	 */
	public static String pmsiErrors = "";
	
	/**
	 * Fonction principale du programme pmsi2sql
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception  {
		
		// D�finition des arguments fournis au programme
		Pmsi2SqlMainOptions options = new Pmsi2SqlMainOptions();
        CmdLineParser parser = new CmdLineParser(options);

        // Lecture des arguments
        try {
            parser.parseArgument(args);

            	// Cr�ation de la connection � la base de donn�es :
            	Connection myConn = options.getNewSqlConnection();
            	// Insertion du fichier en binaire dans la table des fichiers (pmsiinsertion)
            	PmsiInsertion myInsert = new PmsiInsertion(options.getPmsiFile().getPath());
            	// Sauvegarde de l'�tat
            	myInsert.insertSQLLine(myConn);
            	
            	// Indicateur permettant de d�finir quel type de fichier pmsi a pu �tre lu
            	FileType fileResult = FileType.FALSE;
            	// Liste des types de fichiers pmsi pouvant �tre lus
            	List<FileType> fileTypes = new ArrayList<FileType>();
            	Collections.addAll(fileTypes, FileType.RSF, FileType.RSS, FileType.RSF2012);
            	
        		// On essaye de lire le fichier pmsi donn� avec tous les lecteurs dont on dispose,
        		// Le premier qui r�ussit est consid�r� comme le type de fichier auquel on � faire
        		Iterator<FileType> fileTypesit = fileTypes.iterator();
        		boolean mycontinue = true;
        		try {
	            	while (mycontinue) {
	            		fileResult = ReadPMSI(options,  myConn, fileTypesit.next());
	            		if (fileResult != FileType.FALSE || !fileTypesit.hasNext())
	            			mycontinue = false;
	            	}
        		} catch (PmsiFileNotInserable e) {
        			fileResult = FileType.FALSE;
        			pmsiErrors = (e.getMessage() == null ? "" : e.getMessage());
        		}

        		// D�finition de l'�tat de retour de la lecture du fichier : 1 = ok, 0 = fichier non ins�r� correctement
        		BigDecimal myStatus = new BigDecimal(0);
            	switch(fileResult) {
            	case RSS:
            		pmsiErrors =  "Fichier de Type RSS correctement ins�r�";
            		myStatus = new BigDecimal(1);
            		break;
            	case RSF:
            		pmsiErrors =  "Fichier de Type RSF correctement ins�r�";
            		myStatus = new BigDecimal(1);
            		break;
            	case RSF2012:
            		pmsiErrors =  "Fichier de Type RSF2012 correctement ins�r�";
            		myStatus = new BigDecimal(1);
            		break;
            	default:
            	}

            	// Stockage du r�sultat de l'insertion :
            	PmsiInsertionResult myInsertResult = new PmsiInsertionResult(myStatus.toString(), pmsiErrors);
            	myInsertResult.insertSQLLine(myConn);
            	
            	// Cr�ation du lien entre pmsiinsertion et pmsiinsertionresult
            	PreparedStatement myps = myConn.prepareStatement("UPDATE pmsiinsertion SET pmsiinsertionresultid = " +
            			"currval('pmsiinsertionresult_pmsiinsertionresultid_seq') " +
            			"WHERE pmsiinsertionid = currval('pmsiinsertion_pmsiinsertionid_seq');");
        		myps.execute();
        		
        		// Validation de la transaction
            	myConn.commit();
        		System.out.println("Done!\n");
        } catch (CmdLineException e) {
            if(options.isHelp()){
                parser.printUsage(System.out);
            } else if (options.isVersion()){
                System.out.println("Version : 0.1.3");
            } else {
            	parser.setUsageWidth(80);
            	parser.printUsage(System.out);
            	System.out.println(e.getMessage());
            }
        }
	}
	
	/**
	 * Lecture du fichier PMSI 
	 * @param options Options du programme (en particulier le fichier � ins�rer)
	 * @param myConn Connexion � la base de donn�es
	 * @param myType Type de fichier � ins�rer
	 * @return {@link FileType#FALSE} si le fichier n'a pas pu �tre lu, sinon un autre type de {@link FileType}
	 *   correspondant au type de fichier lu si le fichier a pu �tre lu
	 * @throws Exception
	 */
	public static FileType ReadPMSI(Pmsi2SqlMainOptions options, Connection myConn, FileType myType) throws Exception {
		// Sauvegarde de l'�tat de la base de donn�es avant tentative d'insertion de donn�es
		// pour revenir � ce point s'il y a une erreur
		Savepoint mySavePoint = myConn.setSavepoint();
		try {
			// D�finition du type de lecteur selon myType
			PmsiReader r = null;
			switch(myType) {
			case RSS:
				r = new PmsiRSS116Reader(new FileReader(options.getPmsiFile()), myConn);
				break;
			case RSF:
				r = new PmsiRSF2009Reader(new FileReader(options.getPmsiFile()), myConn);
				break;
			case RSF2012:
				r = new PmsiRSF2012Reader(new FileReader(options.getPmsiFile()), myConn);
				break;
			}

			// Lecture du fichier par mise en route de la machine � �tats
        	r.run();
			
        	// Arriv� ici, le fichier a pu �tre lu, on retourne le type de fichier ins�r�
        	return myType;
		} catch (PmsiFileNotReadable e) {
			// Impossible � lire ce fichier avec ce lecteur
			// Insertion du message de cette erreur � la suite des messages pr�c�dents.
			pmsiErrors = pmsiErrors.concat(e.getMessage() + "\n\n");
			// Retour � l'�tat sql pr�c�dant l'appel � la lecture du fichier de type PMSI
			myConn.rollback(mySavePoint);
			// Indication que le lecteur n'a pu lire le fichier pmsi
			return FileType.FALSE;
		} catch (Exception myE) {
			// Toutes les autres erreurs correspondent � des probl�mes qu'on ne peut r�soudre ici
			myConn.rollback(mySavePoint);
			throw myE;
		}
	}
}
