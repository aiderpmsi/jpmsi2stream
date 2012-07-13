package aider.org.pmsi.dto;

import javax.xml.stream.XMLStreamException;

import aider.org.pmsi.parser.linestypes.PmsiLineType;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009Header;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009a;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009b;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009c;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009h;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009m;

public class DtoRsf2009 extends DtoPmsi {

	/**
	 * Construction de la connexion à la base de données à partir des configurations
	 * données
	 * @throws DtoPmsiException 
	 */
	public DtoRsf2009() throws DtoPmsiException {
		super();
	}
	
	/**
	 * Ajoute des données liées à une ligne pmsi
	 * @param lineType ligne avec les données à insérer
	 * @throws DtoPmsiException 
	 * @throws XMLStreamException 
	 */
	public void writeLineElement(PmsiLineType lineType) throws DtoPmsiException {
		try {
			if (lineType instanceof PmsiRsf2009Header) {
				// Ecriture de la ligne header sans la fermer (va contenir les rsf)
				writeSimpleElement(lineType);
				// Prise en compte de l'ouverture de la ligne
				lastLine.add(lineType);
			} else if (lineType instanceof PmsiRsf2009a) {
				// Si un rsfa est ouvert, il faut le fermer
				if (lastLine.lastElement() instanceof PmsiRsf2009a) {
					lastLine.pop();
					xmlWriter.writeEndElement();
				}
				// ouverture du rsfa
				writeSimpleElement(lineType);
				// Prise en compte de l'ouverture de ligne
				lastLine.add(lineType);
			} else if (lineType instanceof PmsiRsf2009b || lineType instanceof PmsiRsf2009c ||
					lineType instanceof PmsiRsf2009h || lineType instanceof PmsiRsf2009m) {
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
