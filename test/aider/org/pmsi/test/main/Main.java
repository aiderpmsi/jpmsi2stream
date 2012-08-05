package aider.org.pmsi.test.main;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import aider.org.pmsi.dto.PmsiStreamMuxer;
import aider.org.pmsi.dto.PmsiThread;
import aider.org.pmsi.exceptions.PmsiReaderException;
import aider.org.pmsi.parser.PmsiRSF2009Reader;
import aider.org.pmsi.parser.PmsiRSF2012Reader;
import aider.org.pmsi.parser.PmsiRSS116Reader;
import aider.org.pmsi.parser.PmsiReader;
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
        
        ArrayList<String> errorsList = new ArrayList<String>();
        
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
        
        // On essaye de lire le fichier pmsi donné avec tous les lecteurs dont on dispose,
        // Le premier qui réussit est considéré comme le bon
        for (FileType fileTypeEntry : listTypes) {
        	try {
        		if (readPMSI(new FileInputStream(options.getPmsiFile()), fileTypeEntry) == true) {
        			break;
        		}
            } catch (Throwable e) {
            	// Si on a une erreur de Reader, c'est que le reader est pas le bon
            	// Si c'est une autre erreur, c'est que le reader est bon, mais que l'insertino
            	// des données après n'a pas marché.
            	if (e instanceof PmsiReaderException) {
            		errorsList.add(((PmsiReaderException) e).getXmlMessage());
            	} else
            		throw e;
            }
        }
	
        for (String hoho : errorsList) {
        	System.out.println(hoho);
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
	public static boolean readPMSI(InputStream in, FileType type) throws Exception {
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
			muxer = new PmsiStreamMuxer();
			
			// Création de lecteur de inputstream et conenction au muxer
			PmsiDtoRunner runner = new PmsiDtoRunner(muxer.getInputStream());
			// Création du thread du lecteur de inputstream
			thread = new PmsiThread(runner);
			
			// Choix du reader et du writer et connection au muxer
			switch(type) {
				case RSS116:
					writer = new Rss116Writer(muxer.getOutputStream());
					reader = new PmsiRSS116Reader(new InputStreamReader(in), writer);
					break;
				case RSF2009:
					writer = new Rsf2009Writer(muxer.getOutputStream());
					reader = new PmsiRSF2009Reader(new InputStreamReader(in), writer);
					break;
				case RSF2012:
					writer = new Rsf2012Writer(muxer.getOutputStream());
					reader = new PmsiRSF2012Reader(new InputStreamReader(in), writer);
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
