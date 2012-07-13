package aider.org.pmsi.parser.main;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import aider.org.pmsi.dto.DTOPmsiReaderFactory;
import aider.org.pmsi.parser.PmsiRSF2009Reader;
import aider.org.pmsi.parser.PmsiRSF2012Reader;
import aider.org.pmsi.parser.PmsiRSS116Reader;
import aider.org.pmsi.parser.PmsiReader;
import aider.org.pmsi.parser.exceptions.PmsiFileNotInserable;
import aider.org.pmsi.parser.exceptions.PmsiFileNotReadable;

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
        
        // Définition de la config de la connexion à la base de données
        DTOPmsiReaderFactory dtoPmsiReaderFactory = new DTOPmsiReaderFactory();

        // Lecture des arguments
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            if(options.isHelp()){
                parser.printUsage(System.out);
                return;
            } else if (options.isVersion()){
                System.out.println("Version : 0.1.9");
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
        		if (readPMSI(options, fileTypeEntry, dtoPmsiReaderFactory) == true) {
        			break;
        		}
            } catch (Throwable e) {
            	if (e instanceof PmsiFileNotReadable || e instanceof PmsiFileNotInserable) {
            		pmsiErrors += (e.getMessage() == null ? "" : e.getMessage());
            	} else
            		throw e;
            }
        }
	
        System.out.println("Done!\n");
	}
	
	/**
	 * Lecture du fichier PMSI 
	 * @param options Options du programme (en particulier le fichier à insérer)
	 * @param type Type de fichier à insérer
	 * @param dtoPmsiReaderFactory Fabrique d'objets de sérialisation
	 * @return true si le fichier a pu être inséré, false sinon
	 * @throws Exception 
	 */
	public static boolean readPMSI(MainOptions options, FileType type, DTOPmsiReaderFactory dtoPmsiReaderFactory) throws Exception {
		PmsiReader<?, ?> reader = null;
		
		try {
			// Choix du reader
			switch(type) {
				case RSS116:
					reader = new PmsiRSS116Reader(new FileReader(options.getPmsiFile()), dtoPmsiReaderFactory);
					break;
				case RSF2009:
					reader = new PmsiRSF2009Reader(new FileReader(options.getPmsiFile()), dtoPmsiReaderFactory);
					break;
				case RSF2012:
					reader = new PmsiRSF2012Reader(new FileReader(options.getPmsiFile()), dtoPmsiReaderFactory);
					break;
				}
	
			// Lecture du fichier par mise en route de la machine à états
	        reader.run();
		} finally {
			if (reader != null)
				reader.close();
		}
			
        // Arrivé ici, le fichier a pu être lu, on retourne true
        return true;
	}
}
