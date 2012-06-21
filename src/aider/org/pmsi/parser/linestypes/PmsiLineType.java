package aider.org.pmsi.parser.linestypes;

import java.util.regex.Pattern;

/**
 * Défini l'architecture pour créer des patrons de lignes pmsi en associant des
 * {@link PmsiElement} à la suite, le tout définissant une suite de caractères à attrapper
 * permettant au final de lire des lignes ou des parties de lignes des fichiers pmsi.
 * @author delabre
 *
 */
public abstract class PmsiLineType {

	/**
	 * Retourne la chaine regex pour attraper les infos de la ligne
	 * @return {@link Pattern} Regex permettant d'attraper la ligne ou la partie de ligne correspondant � ce patron 
	 */
	public abstract Pattern getPattern() ;
	
	public abstract String[] getNames();
	
	public abstract String getName();
	
	public abstract void setContent(int index, String content);
	
	public abstract String[] getContent();
}
