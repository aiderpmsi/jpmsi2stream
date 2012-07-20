package aider.org.pmsi.parser.main;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import aider.org.pmsi.dto.InsertionReport;
import aider.org.pmsi.dto.PmsiStreamMuxer;
import aider.org.pmsi.dto.PmsiStreamRunner;
import aider.org.pmsi.dto.PmsiThread;
import aider.org.pmsi.parser.PmsiRSF2009Reader;
import aider.org.pmsi.parser.PmsiRSF2012Reader;
import aider.org.pmsi.parser.PmsiRSS116Reader;
import aider.org.pmsi.parser.PmsiReader;
import aider.org.pmsi.parser.exceptions.PmsiIOReaderException;
import aider.org.pmsi.parser.exceptions.PmsiIOWriterException;
import aider.org.pmsi.writer.PmsiWriter;
import aider.org.pmsi.writer.Rsf2009Writer;
import aider.org.pmsi.writer.Rsf2012Writer;
import aider.org.pmsi.writer.Rss116Writer;

/**
 * Entrée du programme permettant de lire un fichier pmsi et de le transformer en xml
 * @author delabre 
 *
 */
public class Main {

	/**
	 * Enumération permettant d'indiquer quel lecteur a réussi à réaliser la lecture du fichier
	 * pmsi (et donc de quel type de format le fichier est)
	 * @author delabre
	 */
	public enum FileType {
		RSS116, RSF2009, RSF2012;
	}
	
	/**
	 * Liste des fichiers que l'on peut lire
	 */
	public static List<FileType> listTypes = new ArrayList<Main.FileType>() {
		private static final long serialVersionUID = -4594379149065725315L;
		{
			add(FileType.RSS116);
			add(FileType.RSF2009);
			add(FileType.RSF2012);
		}
	};
	
	/**
	 * Chaine de caractère stockant les erreurs des
	 * lecteurs de fichiers de PMSI
	 */
	public static String pmsiErrors = "";
	
	/**
	 * Fonction principale du programme
	 * @param args
	 * @throws Throwable 
	 */
	public static void main(String[] args) throws Throwable  {
		
		// Définition des arguments fournis au programme
		MainOptions options = new MainOptions();
        CmdLineParser parser = new CmdLineParser(options);
        
        // Lecture des arguments
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            if(options.isHelp()){
                parser.printUsage(System.out);
                return;
            } else if (options.isVersion()){
                System.out.println("Version : 0.0.2");
                return;
            } else {
            	parser.setUsageWidth(80);
            	parser.printUsage(System.out);
            	System.out.println(e.getMessage());
            	return;
            }
        }
        
        // Rapport d'insertion
        InsertionReport report = null;
        // On essaye de lire le fichier pmsi donné avec tous les lecteurs dont on dispose,
        // Le premier qui réussit est considéré comme le bon
        for (FileType fileTypeEntry : listTypes) {
        	try {
        		// Création du rapport d'insertion
        		report = new InsertionReport();
        		readPMSI(new FileInputStream(options.getPmsiFile()), fileTypeEntry, report);
        		break;
            } catch (Throwable e) {
            	if (e instanceof PmsiIOWriterException || e instanceof PmsiIOReaderException) {
            		pmsiErrors += (e.getMessage() == null ? "" : e.getMessage());
            	} else
            		throw e;
            }
        }
	
        System.out.println("Done!\n");
	}
	
	/**
	 * Lecture du fichier PMSI 
	 * @param in flux à insérer
	 * @param type Type de fichier à insérer
	 * @param dtoPmsiReaderFactory Fabrique d'objets de sérialisation
	 * @return true si le fichier a pu être inséré, false sinon
	 * @throws Exception 
	 */
	public static boolean readPMSI(InputStream in, FileType type, InsertionReport report) throws Exception {
		// Reader et writer
		PmsiReader<?, ?> reader = null;
		PmsiWriter writer = null;
		PmsiStreamMuxer muxer = null;
		// Thread du lecteur de writer
		PmsiThread thread = null;
		// exception du lecteur de writer
		Exception exception;
		
		try {
			// Création du transformateur de outputstream en inputstream
			muxer = new PmsiStreamMuxer(report);
			
			// Création de lecteur de inputstream et conenction au muxer
			PmsiStreamRunner runner = new PmsiStreamRunner(muxer.getInputStream(), report);
			// Création du thread du lecteur de inputstream
			thread = new PmsiThread(runner, report);
			
			// Choix du reader et du writer et connection au muxer
			switch(type) {
				case RSS116:
					writer = new Rss116Writer(muxer.getOutputStream(), report);
					reader = new PmsiRSS116Reader(new InputStreamReader(in), writer, report);
					break;
				case RSF2009:
					writer = new Rsf2009Writer(muxer.getOutputStream(), report);
					reader = new PmsiRSF2009Reader(new InputStreamReader(in), writer, report);
					break;
				case RSF2012:
					writer = new Rsf2012Writer(muxer.getOutputStream(), report);
					reader = new PmsiRSF2012Reader(new InputStreamReader(in), writer, report);
					break;
				}
			
			// lancement du lecteur de muxer
			thread.start();
	
			// Lecture du fichier par mise en route de la machine à états
			reader.run();
			
			// Fin de fichier evoyé au muxer
			muxer.eof();
	
			// Attente que le lecteur de muxer ait fini
			thread.waitEndOfProcess();
						
		} finally {
			// Fermeture de resources
			if (reader != null)
				reader.close();
			if (writer != null)
				writer.close();
			if (muxer != null)
				muxer.close();
		}

		// Récupération d'une erreur éventuelle du lecteur de muxer
		exception = thread.getTerminalException();
		
		if (exception != null)
			throw exception;

		return true;
	}
}
