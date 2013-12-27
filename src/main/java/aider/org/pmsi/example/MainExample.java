package aider.org.pmsi.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import aider.org.machinestate.MachineStateException;
import aider.org.pmsi.exceptions.PmsiParserException;
import aider.org.pmsi.exceptions.PmsiWriterException;
import aider.org.pmsi.parser.PmsiRSF2009Parser;
import aider.org.pmsi.parser.PmsiRSF2012Parser;
import aider.org.pmsi.parser.PmsiRSS116Parser;
import aider.org.pmsi.parser.PmsiParser;
import aider.org.pmsi.writer.PmsiWriter;
import aider.org.pmsi.writer.PmsiXmlWriter;

/**
 * Entrée du programme permettant de lire un fichier pmsi et de le transformer en xml
 * @author delabre 
 *
 */
public class MainExample {

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
	public static List<FileType> listTypes = new ArrayList<MainExample.FileType>() {
		private static final long serialVersionUID = -4594379149065725315L;
		{
		//	add(FileType.RSS116);
		//	add(FileType.RSF2009);
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
	 * @throws FileNotFoundException 
	 * @throws PmsiWriterException 
	 * @throws MachineStateException 
	 * @throws Throwable 
	 */
	public static void main(String[] args) throws PmsiWriterException, FileNotFoundException, MachineStateException {
		
		// Définition des arguments fournis au programme
		MainExampleOptions options = new MainExampleOptions();
        CmdLineParser parser = new CmdLineParser(options);
        
        ArrayList<String> parsersReportErrorsList = new ArrayList<String>();
        
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
            } catch (MachineStateException e) {
            	// Si l'erreur parente est PmsiParserException, c'est que le parseur n'a
            	// juste pas été capable de déchiffrer le fichier, tout le reste marchait.
            	// Il faut donc essayer avec un autre parseur
            	if (e.getCause() instanceof PmsiParserException) {
            		parsersReportErrorsList.add(
            				e.getStackTrace()[0].getClassName() + " : " +
            				e.getCause().getMessage());
            	} else
            		throw e;
            }
        }
	
        for (String hoho : parsersReportErrorsList) {
        	System.out.println(hoho);
        }
        
        System.out.println("Done!\n");
	}
	
	/**
	 * Lecture du fichier PMSI 
	 * @param in flux à insérer
	 * @param type Type de fichier à insérer
	 * @return true si le fichier a pu être inséré, false sinon
	 * @throws PmsiWriterException 
	 * @throws MachineStateException 
	 * @throws Exception 
	 */
	public static boolean readPMSI(InputStream in, FileType type) throws PmsiWriterException, MachineStateException {
		// Reader et writer
		PmsiParser<?, ?> parser = null;
		PmsiWriter writer = new PmsiXmlWriter(System.out, "UTF-8");
		
		try {
			// Choix du parser et du writer
			switch(type) {
				case RSS116:
					parser = new PmsiRSS116Parser(new InputStreamReader(in), writer);
					break;
				case RSF2009:
					parser = new PmsiRSF2009Parser(new InputStreamReader(in), writer);
					break;
				case RSF2012:
					parser = new PmsiRSF2012Parser(new InputStreamReader(in), writer);
					break;
				}
			
			// lancement du parseur
			parser.call();
			
			return true;
		} finally {
			// Fermeture de resources
			if (parser != null)
				parser.close();
			if (writer != null)
				writer.close();
		}
	}
}
