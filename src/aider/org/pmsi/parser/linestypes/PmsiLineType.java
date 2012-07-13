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
	
	/**
	 * Retourne la liste des noms des éléments de cette ligne
	 * @return
	 */
	public abstract String[] getNames();
	
	/**
	 * Retourne le nom d'identification de la classe
	 * @return
	 */
	public abstract String getName();
	
	/**
	 * Définit la valeur <code>content</code> au paramètre à la position <code>index</code>
	 * @param index
	 * @param content
	 */
	public abstract void setContent(int index, String content);
	
	/**
	 * Récupère la liste des contenus de cet élément pmsi
	 * @return
	 */
	public abstract String[] getContent();
}
