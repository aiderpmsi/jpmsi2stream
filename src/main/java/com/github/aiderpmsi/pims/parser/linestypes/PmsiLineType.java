package com.github.aiderpmsi.pims.parser.linestypes;

import java.io.IOException;

import org.xml.sax.ContentHandler;

import com.github.aiderpmsi.pims.parser.utils.MemoryBufferedReader;

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
public abstract class PmsiLineType {
	
	@FunctionalInterface
	public interface LineWriter {
		public void writeResults(PmsiLineType pmsiLineType, ContentHandler contentHandler) throws IOException;
	}

	private LineWriter lineWriter;
	
	public PmsiLineType(LineWriter lineWriter) {
		this.lineWriter = lineWriter;
	}
	
	/**
	 * Checks if this line is a valid line
	 * @return
	 */
	public abstract boolean isFound(MemoryBufferedReader br) throws IOException;

	/**
	 * Maps the line to content writen to the content handler and removes it from the Reader
	 * @param contentHandler
	 * @throws IOException
	 */
	public void writeResults(ContentHandler contentHandler) throws IOException {
		lineWriter.writeResults(this, contentHandler);
	}

}
