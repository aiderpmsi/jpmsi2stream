package org.aider.pmsi2sql;

import java.io.File;

import org.kohsuke.args4j.Option;

/**
 * Rajoute les arguments suivants pour le lancement du programme :
 * <li>Fichier de PMSI<li>
 * <p>
 * <code>-f (--file) FILE</code>
 * <p>
 * D�finit le fichier PMSI  (type RSS v6 ou RSF nouvelle version) � ins�rer
 * </ul>
 * @author delabre
 */
public class Pmsi2SqlMainOptions extends Pmsi2SqlBaseOptions{

    /**
     * D�finit le fichier de PMSI � importer
     */
    @Option(name = "-f", aliases = {"--file"}, metaVar = "FILE", usage = "Specifie le fichier de PMSI � ins�rer", required=true)
    private File pmsifile;
    /**
     * Renvoie le fichier de PMSI (RSS ou RSF) � ins�rer dans la base de donn�es
     * @return Fichier PMSI
     */
    public File getPmsiFile() {
    	return pmsifile;
    }
}

