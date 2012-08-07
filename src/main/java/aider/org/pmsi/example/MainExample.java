package aider.org.pmsi.example;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import aider.org.pmsi.dto.PmsiCallable;
import aider.org.pmsi.exceptions.PmsiReaderException;
import aider.org.pmsi.parser.PmsiRSF2009Parser;
import aider.org.pmsi.parser.PmsiRSF2012Parser;
import aider.org.pmsi.parser.PmsiRSS116Parser;
import aider.org.pmsi.parser.PmsiParser;
import aider.org.pmsi.writer.PmsiWriter;
import aider.org.pmsi.writer.PmsiXmlPipedOutputStreamWriter;
import aider.org.pmsi.writer.Rsf2009Writer;
import aider.org.pmsi.writer.Rsf2012Writer;
import aider.org.pmsi.writer.Rss116Writer;

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
		MainExampleOptions options = new MainExampleOptions();
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
	 * @return true si le fichier a pu être inséré, false sinon
	 * @throws Exception 
	 */
	public static boolean readPMSI(InputStream in, FileType type) throws Exception {
		// Reader et writer
		PmsiParser<?, ?> reader = null;
		PmsiWriter writer = null;
		
		// Flux output connecté
		PipedOutputStream outputStream = null;
		
		// Thread du lecteur de writer
		PmsiCallable pmsiCallable = null;
		// exception du lecteur de writer
		Exception exception = null;
		// Thread executor
		ExecutorService threadExecutor = Executors.newCachedThreadPool();
		Future<String> threadDtoFuture = null;
		Future<String> threadParserFuture = null;
		
		try {		
			// Choix du parser et du writer
			switch(type) {
				case RSS116:
					writer = new Rss116Writer();
					reader = new PmsiRSS116Parser(new InputStreamReader(in), writer);
					break;
				case RSF2009:
					writer = new Rsf2009Writer();
					reader = new PmsiRSF2009Parser(new InputStreamReader(in), writer);
					break;
				case RSF2012:
					writer = new Rsf2012Writer();
					reader = new PmsiRSF2012Parser(new InputStreamReader(in), writer);
					break;
				}
			
			// Création de la classe de transfert de données
			// Et connection au flux de sortie du writer
			PmsiDtoExample pmsiDto = new PmsiDtoExample(System.out);
			((PmsiXmlPipedOutputStreamWriter) writer).getOutputStream().connect(pmsiDto.getPipedInputStream());
			
			// Création du thread du lecteur de inputstream
			pmsiCallable = new PmsiCallable(pmsiDto);
			
			// lancement du lecteur de muxer
			threadDtoFuture = threadExecutor.submit(pmsiCallable);
	
			// lancement du parseur
			threadParserFuture = threadExecutor.submit(reader);
			
			// Attente que le parseur ait fini
			threadParserFuture.get();
			
			// Fermeture de la classe de transfert de données
			pmsiDto.close();
	
			// Attente que le lecteur de muxer ait fini
			threadDtoFuture.get();
						
		} catch (ExecutionException e) {
			exception = (Exception) e.getCause();
		} catch (InterruptedException e) {
			exception = e;
		} finally {
			// Fermeture de resources
			if (reader != null)
				reader.close();
			if (writer != null)
				writer.close();
			if (outputStream != null)
				outputStream.close();
		}
		
		if (exception != null)
			throw exception;

		return true;
	}
}
