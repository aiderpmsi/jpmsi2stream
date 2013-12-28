package com.github.aiderpmsi.jpmi2stream.main.options;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

public class DefaultParserOptions {

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
     * Définit l'url du fichier de PMSI à traiter
     */
    @Option(name = "-u", aliases = {"--url"}, metaVar = "URL", usage = "URL du fichier PMSI à traiter", required=true)
    private URL pmsiurl;
    
    /**
     * Renvoie l'url du fichier de PMSI à traiter
     * @return Fichier PMSI
     */
    public URL getPmsiUrl() {
    	return pmsiurl;
    }
    
    /**
     * Définit le chemin du fichier d'export
     */
    @Option(name = "-o", aliases = {"--outputfile"}, metaVar = "FILE", usage = "Chemin du fichier d'export", required=true)
    private File exportfile;
    
    /**
     * Renvoie le chemin du fichier d'export
     * @return Fichier d'export
     */
    public File getExportFile() {
    	return exportfile;
    }

    /**
     * Liste des arguments non traités
     */
    @Argument
    private List<String> argument;
    
    /**
     * Renvoie la liste des arguments non traités
     * @return {@link List}<{@link String}> Arguments non traités
     */
    public List<String> getArguments() {
    	return argument;   
   }

}
