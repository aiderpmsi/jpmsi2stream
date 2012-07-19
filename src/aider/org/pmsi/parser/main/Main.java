package aider.org.pmsi.parser.main;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import aider.org.pmsi.dto.PmsiDtoFactory;
import aider.org.pmsi.dto.PmsiDtoReportError;
import aider.org.pmsi.dto.PmsiDtoReportFactory;
import aider.org.pmsi.dto.PmsiThreadedPipedReaderFactory;
import aider.org.pmsi.dto.PmsiPipedWriterFactory;
import aider.org.pmsi.parser.PmsiRSF2009Reader;
import aider.org.pmsi.parser.PmsiRSF2012Reader;
import aider.org.pmsi.parser.PmsiRSS116Reader;
import aider.org.pmsi.parser.PmsiReader;
import aider.org.pmsi.parser.exceptions.PmsiIOException;
import aider.org.pmsi.parser.exceptions.PmsiPipedIOException;

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
        
        // Définition de la fabrique d'objet de rapport de l'envoi des données dans le système récepteur
        PmsiDtoReportFactory reportFactory = new PmsiDtoReportFactory();
        
        // Définition de la fabrique de Transfert de données entre le Reader et le système récepteur
        PmsiDtoFactory dtoFactory = new PmsiDtoFactory(reportFactory);
        
        // Définition de la fabrique de Reader de pmsi
        PmsiThreadedPipedReaderFactory pipedReaderFactory = new PmsiThreadedPipedReaderFactory(dtoFactory);
        
        // Définition de la fabrique de Writer de pmsi
        PmsiPipedWriterFactory dtoPmsiReaderFactory = new PmsiPipedWriterFactory(pipedReaderFactory);

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
        		if (readPMSI(new FileInputStream(options.getPmsiFile()), fileTypeEntry, dtoPmsiReaderFactory) == true) {
        			break;
        		}
            } catch (Throwable e) {
            	if (e instanceof PmsiPipedIOException || e instanceof PmsiIOException) {
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
	public static boolean readPMSI(InputStream in, FileType type, PmsiPipedWriterFactory dtoPmsiReaderFactory) throws Exception {
		PmsiReader<?, ?> reader = null;
		// Choix du reader
		switch(type) {
			case RSS116:
				reader = new PmsiRSS116Reader(new InputStreamReader(in), dtoPmsiReaderFactory);
				break;
			case RSF2009:
				reader = new PmsiRSF2009Reader(new InputStreamReader(in), dtoPmsiReaderFactory);
				break;
			case RSF2012:
				reader = new PmsiRSF2012Reader(new InputStreamReader(in), dtoPmsiReaderFactory);
				break;
			}
		
		try {
			// Lecture du fichier par mise en route de la machine à états
			reader.run();
		} catch (Exception ignore) {}
        
		try {
	        // Fermeture des données de connection externes :
	        reader.close();
		} catch (Exception ignore) {}
        
        // Ecriture des erreurs éventuelles d'insertion :
        HashMap<PmsiDtoReportError, Object> errors = reader.getReport();
        for (PmsiDtoReportError key : errors.keySet()) {
        	System.out.println(key.getOrigin().toString() + " : " + key.getName());
        	System.out.println(errors.get(key));
        }
        
        // Retour du satut d'insertion ou non :
        return reader.getStatus();
	}
}
