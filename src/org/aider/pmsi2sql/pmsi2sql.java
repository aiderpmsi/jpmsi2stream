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

/**
 * Entrée du programme permettant d'insérer un fichier pmsi dans la base de données adéquate. Contient
 * la fonction main de pmsi2sql
 * @author delabre
 *
 */
public class pmsi2sql {

	/**
	 * Enumération permettant d'indiquer quel lecteur a réussi à réaliser la lecture du fichier
	 * pmis (et donc de quel type de format le fichier est
	 * @author delabre
	 *
	 */
	public enum FileType {
		FALSE, RSS, RSF;
	}
	
	/**
	 * Chaine de caractère stockant les erreurs des
	 * lecteurs de fichiers de PMSI
	 */
	public static String pmsiErrors = "";
	
	/**
	 * Fonction principale du programme pmsi2sql
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception  {
		
		// Définition des arguments fournis au programme
		Pmsi2SqlMainOptions options = new Pmsi2SqlMainOptions();
        CmdLineParser parser = new CmdLineParser(options);

        // Lecture des arguments
        try {
            parser.parseArgument(args);

            	// Création de la connection à la base de données :
            	Connection myConn = options.getNewSqlConnection();
            	// Insertion du fichier en binaire dans la table des fichiers (pmsiinsertion)
            	PmsiInsertion myInsert = new PmsiInsertion(options.getPmsiFile().getPath());
            	// Sauvegarde de l'état
            	myInsert.insertSQLLine(myConn);
            	
            	// Indicateur permettant de définir quel type de fichier pmsi a pu être lu
            	FileType fileResult = FileType.FALSE;
            	// Liste des types de fichiers pmsi pouvant être lus
            	List<FileType> fileTypes = new ArrayList<FileType>();
            	Collections.addAll(fileTypes, FileType.RSF, FileType.RSS);
            	
        		// On essaye de lire le fichier pmsi donné avec tous les lecteurs dont on dispose,
        		// Le premier qui réussit est considéré comme le type de fichier auquel on à faire
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

        		// Définition de l'état de retour de la lecture du fichier : 1 = ok, 0 = fichier non inséré correctement
        		BigDecimal myStatus = new BigDecimal(0);
            	switch(fileResult) {
            	case RSS:
            		pmsiErrors =  "Fichier de Type RSS correctement inséré";
            		myStatus = new BigDecimal(1);
            		break;
            	case RSF:
            		pmsiErrors =  "Fichier de Type RSF correctement inséré";
            		myStatus = new BigDecimal(1);
            		break;
            	default:
            	}

            	// Stockage du résultat de l'insertion :
            	PmsiInsertionResult myInsertResult = new PmsiInsertionResult(myStatus.toString(), pmsiErrors);
            	myInsertResult.insertSQLLine(myConn);
            	
            	// Création du lien entre pmsiinsertion et pmsiinsertionresult
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
                System.out.println("Version : 0.1.2");
            } else {
            	parser.setUsageWidth(80);
            	parser.printUsage(System.out);
            	System.out.println(e.getMessage());
            }
        }
	}
	
	/**
	 * Lecture du fichier PMSI 
	 * @param options Options du programme (en particulier le fichier à insérer)
	 * @param myConn Connexion à la base de données
	 * @param myType Type de fichier à insérer
	 * @return {@link FileType#FALSE} si le fichier n'a pas pu être lu, sinon un autre type de {@link FileType}
	 *   correspondant au type de fichier lu si le fichier a pu être lu
	 * @throws Exception
	 */
	public static FileType ReadPMSI(Pmsi2SqlMainOptions options, Connection myConn, FileType myType) throws Exception {
		// Sauvegarde de l'état de la base de données avant tentative d'insertion de données
		// pour revenir à ce point s'il y a une erreur
		Savepoint mySavePoint = myConn.setSavepoint();
		try {
			// Définition du type de lecteur selon myType
			PmsiReader r = null;
			switch(myType) {
			case RSS:
				r = new PmsiRSSReader(new FileReader(options.getPmsiFile()), myConn);
				break;
			case RSF:
				r = new PmsiRSFReader(new FileReader(options.getPmsiFile()), myConn);
				break;
			}

			// Lecture du fichier par mise en route de la machine à états
        	r.run();
			
        	// Arrivé ici, le fichier a pu être lu, on retourne le type de fichier inséré
        	return myType;
		} catch (PmsiFileNotReadable e) {
			// Impossible à lire ce fichier avec ce lecteur
			// Insertion du message de cette erreur à la suite des messages précédents.
			pmsiErrors = pmsiErrors.concat(e.getMessage() + "\n");
			// Retour à l'état sql précédant l'appel à la lecture du fichier de type PMSI
			myConn.rollback(mySavePoint);
			// Indication que le lecteur n'a pu lire le fichier pmsi
			return FileType.FALSE;
		} catch (Exception myE) {
			// Toutes les autres erreurs correspondent à des problèmes qu'on ne peut résoudre ici
			myConn.rollback(mySavePoint);
			throw myE;
		}
	}
}
