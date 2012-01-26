package org.aider.pmsi2sql;

import java.io.File;

import org.kohsuke.args4j.Option;

/**
 * Rajoute les arguments suivants pour le lancement du programme :
 * <li>Fichier de PMSI<li>
 * <p>
 * <code>-f (--file) FILE</code>
 * <p>
 * Définit le fichier PMSI  (type RSS v6 ou RSF nouvelle version) à insérer
 * </ul>
 * @author delabre
 */
public class Pmsi2SqlMainOptions extends Pmsi2SqlBaseOptions{

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
}

