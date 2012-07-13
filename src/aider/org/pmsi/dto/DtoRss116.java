package aider.org.pmsi.dto;

import aider.org.pmsi.parser.linestypes.PmsiLineType;
import aider.org.pmsi.parser.linestypes.PmsiRss116Acte;
import aider.org.pmsi.parser.linestypes.PmsiRss116Da;
import aider.org.pmsi.parser.linestypes.PmsiRss116Dad;
import aider.org.pmsi.parser.linestypes.PmsiRss116Header;
import aider.org.pmsi.parser.linestypes.PmsiRss116Main;

public class DtoRss116 extends DtoPmsi {

	/**
	 * Construction de la connexion à la base de données à partir des configurations
	 * données
	 * @throws DtoPmsiException 
	 */
	public DtoRss116() throws DtoPmsiException {
		super();
	}
	
	/**
	 * Ajoute des données liées à une ligne pmsi
	 * @param lineType ligne avec les données à insérer
	 * @throws DtoPmsiException 
	 */
	public void writeLineElement(PmsiLineType lineType) throws DtoPmsiException  {
		try {
			if (lineType instanceof PmsiRss116Header) {
				// Ecriture de la ligne header sans la fermer (va contenir les rss)
				writeSimpleElement(lineType);
				// Prise en compte de l'ouverture de la ligne
				lastLine.add(lineType);
			} else if (lineType instanceof PmsiRss116Main) {
				// Si un rss main est ouvert, il faut le fermer
				if (lastLine.lastElement() instanceof PmsiRss116Main) {
					lastLine.pop().getName();
					xmlWriter.writeEndElement();
				}
				// ouverture du rss main
				writeSimpleElement(lineType);
				// Prise en compte de l'ouverture de ligne
				lastLine.add(lineType);
			} else if (lineType instanceof PmsiRss116Acte || lineType instanceof PmsiRss116Da ||
					lineType instanceof PmsiRss116Dad) {
				// Ouverture de la ligne
				writeSimpleElement(lineType);
				// fermeture de la ligne
				xmlWriter.writeEndElement();
			}
		} catch (Exception e) {
			throw new DtoPmsiException(e);
		}
	}
}
