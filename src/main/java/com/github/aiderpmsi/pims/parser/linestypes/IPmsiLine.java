package com.github.aiderpmsi.pims.parser.linestypes;

import java.io.IOException;
import java.util.Collection;

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
	 * Gets the declared name of this line
	 * @return Name of this line (multiple version of a same line name can exist)
	 */
	public String getName();
	
	/**
	 * Checks if this line is a valid line
	 * @return
	 */
	public abstract boolean matches(final Segment line) throws IOException;

	/**
	 * Gets the number of chars needed for this kind of line
	 * @return
	 */
	public int getLineSize();
	
	/**
	 * Returns the last matched line
	 * @return
	 * @throws IOException
	 */
	public Segment getMatchedLine();
	
	/**
	 * 
	 * @return returns the ordered elements of this line
	 * @throws IOException
	 */
	public Collection<Element> getElements();

	/**
	 * Returns the version of this line
	 * @return
	 */
	public String getVersion();
	
	/**
	 * An element in Pmsi Line
	 * @author jpc
	 *
	 */
	public class Element {
		
		private final String name;

		private final Segment element;

		public Element(final String name, final Segment element) {
			super();
			this.name = name;
			this.element = element;
		}

		public String getName() {
			return name;
		}

		public Segment getElement() {
			return element;
		}

		
	}
}
