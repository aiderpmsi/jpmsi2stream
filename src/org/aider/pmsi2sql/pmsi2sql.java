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
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

public class pmsi2sql {

	public enum FileType {
		FALSE, RSS, RSF;
	}
	
	/**
	 * Chaine de caractère stockant les différentes erreurs des différents
	 * lecteurs de fichiers de PMSI
	 */
	public static String pmsiErrors = "";
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		// Définition des arguments fournis au programme
		Pmsi2SqlMainOptions options = new Pmsi2SqlMainOptions();
        CmdLineParser parser = new CmdLineParser(options);

        // Lecture des arguments
        try {
            parser.parseArgument(args);

            if(options.isHelp()){
                parser.printUsage(System.out);
            } else if (options.isVersion()){
                System.out.println("Version");
            } else {
            	// Création de la connection à la base de données :
            	Connection myConn = options.getNewSqlConnection();
            	// Insertion du fichier en binaire dans la table des fichiers (pmsiinsertion)
            	PmsiInsertion myInsert = new PmsiInsertion(options.getPmsiFile().getPath());
            	// Sauvegarde de l'état
            	myInsert.insertSQLLine(myConn);
            	
            	FileType fileResult = FileType.FALSE;
            	List<FileType> fileTypes = new ArrayList<FileType>();
            	Collections.addAll(fileTypes, FileType.RSF, FileType.RSS);
            	Iterator<FileType> fileTypesit = fileTypes.iterator();

            	// Stockage du message de compte-rendu dans la base de données :
            	PreparedStatement myps = myConn.prepareStatement("UPDATE pmsiinsertion SET status = ?, log = ? " +
            			"WHERE pmsiinsertionid = currval('pmsiinsertion_pmsiinsertionid_seq');");
        		myps.setBigDecimal(1, new BigDecimal(0));

        		try {
            	boolean mycontinue = true;
            	while (mycontinue) {
            		fileResult = ReadPMSI(options,  myConn, fileTypesit.next());
            		if (fileResult != FileType.FALSE || !fileTypesit.hasNext())
            			mycontinue = false;
            	}
        		} catch (PmsiFileNotInserable e) {
        			fileResult = FileType.FALSE;
        			pmsiErrors = (e.getMessage() == null ? "" : e.getMessage());
        		}

            	switch(fileResult) {
            	case RSS:
            		pmsiErrors =  "Fichier de Type RSS correctement inséré";
            		myps.setBigDecimal(1, new BigDecimal(1));
            		break;
            	case RSF:
            		pmsiErrors =  "Fichier de Type RSF correctement inséré";
            		myps.setBigDecimal(1, new BigDecimal(1));
            		break;
            	default:
            	}
            	myps.setString(2, pmsiErrors);
            	myps.execute();
            	myConn.commit();
            }

        } catch (CmdLineException e) {
            parser.setUsageWidth(80);
            parser.printUsage(System.out);
            System.out.println(e.getMessage());
        }
		System.out.println("Done!\n");
	}
	
	public static FileType ReadPMSI(Pmsi2SqlMainOptions options, Connection myConn, FileType myType) throws Exception {
		// Sauvegarde de l'état de la base de données
		Savepoint mySavePoint = myConn.setSavepoint();
		try {
			PmsiReader r = null;

			switch(myType) {
			case RSS:
				r = new PmsiRSSReader(new FileReader(options.getPmsiFile()), myConn);
				break;
			case RSF:
				r = new PmsiRSFReader(new FileReader(options.getPmsiFile()), myConn);
				break;
			}

        	r.run();
			
        	return myType;
		} catch (PmsiFileNotReadable e) {
			// Impossible à lire ce fichier avec ce lecteur
			// On peuple le stack de log avec le message de cette erreur
			pmsiErrors = pmsiErrors.concat(e.getMessage() + "\n");
			// On revient à l'état sql sauvegardé au début de la fonction
			myConn.rollback(mySavePoint);
			return FileType.FALSE;
		} catch (Exception e) {
			myConn.rollback(mySavePoint);
			throw e;
		}
	}
}
