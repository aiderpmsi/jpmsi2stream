package org.aider.pmsi2sql;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

import org.aider.pmsi2sql.linetypes.PmsiInsertion;
import org.aider.pmsi2sql.linetypes.PmsiInsertionResult;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import aider.org.pmsi.reader.PmsiRSF2009Reader;
import aider.org.pmsi.reader.PmsiRSS116Reader;

/**
 * Classe principale du programme pmsi2sqlcreate qui permet de cr�er les
 * tables pour utiliser l'application pmsi2sql
 * @author delabre
 *
 */
public class Pmsi2SqlCreate {

	/**
	 * Fonction principale du programme pmsi2sqlcreate
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		// D�finition des arguments fournis au programme
		Pmsi2SqlBaseOptions options = new Pmsi2SqlBaseOptions();
        CmdLineParser parser = new CmdLineParser(options);

        // Tentative de lecture de la ligne de commande
        try {
        	parser.parseArgument(args);
        } catch (CmdLineException e) {
        	// Au moins un argument requis est manquant
        	// Si on a appel� l'aide ou le num�ro de version, il est normal qu'un argument manque,
        	// Sinon on affiche le message indiquant les arguments possibles ainsi que l'argument
        	// manquant
        	if(!options.isHelp() && !options.isVersion()) {
        		parser.setUsageWidth(80);
        		parser.printUsage(System.out);
        		System.out.println(e.getMessage());
        		// Sortie de la fonction principale du programme
        		return;
            }
        }

        // La ligne de commande a bien �t� lue, ou bien mal mais parceque l'argument 'help' ou
        // 'version' a �t� indiqu�
    	if(options.isHelp()){
    		// L'affichage de l'aide a �t� demand�e
    		parser.setUsageWidth(80);
            parser.printUsage(System.out);
            // Sortie du programme
            return;
        } else if (options.isVersion()){
        	// L'affichage de la version a �t� demand�
    		parser.setUsageWidth(80);
            System.out.println("Version : 12.0");
            // sortie du programme
            return;
        }

    	// Connexion � la base de donn�es selon les arguments de la ligne de commande
       	Connection myConn = options.getNewSqlConnection();
       	
       	// Cr�ation de la table qui trace le r�sultat des tentatives d'insertion de fichiers pmsi
       	PmsiInsertionResult myInsertionResultTable = new PmsiInsertionResult("", "");
       	// Cr�ation de la table qui trace les tentatives d'insertion de fichiers pmsi
    	PmsiInsertion myInsertionTable = new PmsiInsertion("");
    	Savepoint point = myConn.setSavepoint();
    	
    	try {
       		myConn.createStatement().execute(myInsertionResultTable.getSQLTable());
       		myConn.createStatement().execute(myInsertionResultTable.getSQLIndex());
       		myConn.createStatement().execute(myInsertionResultTable.getSQLFK());

	    	myConn.createStatement().execute(myInsertionTable.getSQLTable());
	    	myConn.createStatement().execute(myInsertionTable.getSQLIndex());
	    	myConn.createStatement().execute(myInsertionTable.getSQLFK());
    	} catch (SQLException e) {
    		if (e.getSQLState().equals("42P16") || e.getSQLState().equals("42P07") ||
    				e.getSQLState().equals("42710")) {
    			// La structure a d�j� �t� cr��e, on retourne au savepoint et
				// on continue
    			myConn.rollback(point);
    		} else {
				System.out.println(e.getSQLState());
				throw e;
			}

    	}
    	// Cr�ation des tables permettant de stocker les RSS
    	PmsiRSS116Reader r = new PmsiRSS116Reader(new StringReader(""), myConn);
    	r.createTables();
    	r.createIndexes();
    	r.createKF();
    	
    	// Cr�ation des tables permettant de stocker les RSF
		PmsiRSF2009Reader f = new PmsiRSF2009Reader(new StringReader(""), myConn);
		f.createTables();
		f.createIndexes();
		f.createKF();

		// Cr�ation des tables permettant de stocker les RSF2012
		PmsiRSF2012Reader f2012 = new PmsiRSF2012Reader(new StringReader(""), myConn);
		f2012.createTables();
		f2012.createIndexes();
		f2012.createKF();
		
		//Fermeture de la connexion � la base de donn�es
		myConn.commit();
    	myConn.close();

    	// Affichage de la r�ussite du programme
        System.out.println("pmsi2sqlcreate successfully finished!");
	}
}
