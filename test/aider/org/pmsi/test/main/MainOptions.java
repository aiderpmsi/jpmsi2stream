package aider.org.pmsi.test.main;

import java.io.File;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

/**
 * Les options sont les suivantes :
 * <ul>
 * <li>Aide</li>
 * <code>-h (--help)</code>
 * <li>Numéro de version</li>
 * <code>-v (--version)</code>
 * <li>Fichier de log</li>
 * <code>-l (--logfile)</code>
 * @author delabre
 *
 */
public class MainOptions {

	/**
	 * Définit si l'argument de demande d'affichage de l'aide a été défini
	 * dans la liste des arguments
	 */
	@Option(name = "-h", aliases = {"--help"}, usage = "Affiche l'aide")
    private boolean help;
	
	/**
	 * Renvoie si l'argument de demande d'affichage de l'aide a été défini
	 * dans la liste des arguments
	 * @return <code>true</code> si oui, <code>false</code> si non
	 */
    public boolean isHelp() {
    	return help;
    }

    /**
	 * Définit si l'argument de demande d'affichage de la version a été défini
	 * dans la liste des arguments
	 */
    @Option(name = "-v", aliases = {"--version"}, usage = "Affiche la version")
    private boolean version;
    
    /**
	 * Renvoie si l'argument de demande d'affichage de la version a été défini
	 * dans la liste des arguments
	 * @return <code>true</code> si oui, <code>false</code> si non
	 */
    public boolean isVersion() {
    	return version;
    }

    /**
     * Définit le fichier de log pour tracer les erreurs du programme (en dehors de celles
     * qui sont insérées dans la base de données)
     */
    @Option(name = "-l", aliases = {"--logfile"}, metaVar = "FILE", usage = "Specifie le fichier de trace (non utilisé)")
    private File logfile;

    /**
     * Renvoie le fichier de log utilisé pour tracer les erreurs du programme (en dehors de celles
     * qui sont insérées dans la base de données). Pour l'instant non utilisé.
     * @return {@link File} Fichier de Log
     */
    public File getLogFile() {
    	return logfile;
    }
    
    /**
     * Définit le fichier de PMSI à importer
     */
    @Option(name = "-f", aliases = {"--file"}, metaVar = "FILE", usage = "Specifie le fichier de PMSI à insérer", required=true)
    private File pmsifile;
    
    /**
     * Renvoie le fichier de PMSI (RSS ou RSF) à insérer dans la base de données
     * @return Fichier PMSI
     */
    public File getPmsiFile() {
    	return pmsifile;
    }
    
    /**
     * Liste des arguments non traités
     */
    @Argument
    private List<String> argument;
    
    /**
     * Renvoie la liste des arguments non traités
     * @return {@link List}<{@link String}> Arguments non trait�s
     */
    public List<String> getArguments() {
    	return argument;   
   }
}

