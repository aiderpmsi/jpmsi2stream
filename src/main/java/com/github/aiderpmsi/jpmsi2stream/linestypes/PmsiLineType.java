package com.github.aiderpmsi.jpmsi2stream.linestypes;

import java.io.IOException;
import org.xml.sax.ContentHandler;

import com.github.aiderpmsi.jpmi2stream.MemoryBufferedReader;

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
public interface PmsiLineType {
	
	/**
	 * Checks if thie line is a valid line
	 * @return
	 */
	public boolean isFound(MemoryBufferedReader br) throws IOException;

	/**
	 * Maps the line to content writen to the content handler
	 * @param contentHandler
	 * @throws IOException
	 */
	public void writeResults(ContentHandler contentHandler) throws IOException;
	
}
