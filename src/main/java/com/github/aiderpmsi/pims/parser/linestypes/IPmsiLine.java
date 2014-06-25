package com.github.aiderpmsi.pims.parser.linestypes;

import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * Défini l'architecture pour créer des patrons de lignes pmsi avec :
 * <ul>
 *  <li>Les noms des éléments</li>
 *  <li>Les regex des éléments</li>
 *  <li>Les contenus des éléments</li>
 * </ul>
 * @author delabre
 *
 */
public interface IPmsiLine {
		
	/**
	 * Checks if this line is a valid line
	 * @return
	 */
	public abstract boolean matches(Segment line) throws IOException;

	/**
	 * Gets the number of chars needed for this kind of line
	 * @return
	 */
	public int getLineSize();
	
	public LinkedHashMap<String, Segment> getResults() throws IOException;

	public String getName();
}
