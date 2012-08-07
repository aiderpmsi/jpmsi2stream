package aider.org.pmsi.writer;

import aider.org.pmsi.exceptions.PmsiWriterException;
import aider.org.pmsi.parser.PmsiRSS116Parser;
import aider.org.pmsi.parser.linestypes.PmsiLineType;
import aider.org.pmsi.parser.linestypes.PmsiRss116Acte;
import aider.org.pmsi.parser.linestypes.PmsiRss116Da;
import aider.org.pmsi.parser.linestypes.PmsiRss116Dad;
import aider.org.pmsi.parser.linestypes.PmsiRss116Header;
import aider.org.pmsi.parser.linestypes.PmsiRss116Main;

/**
 * Writer {@link PmsiXmlPipedOutputStreamWriter} pour {@link PmsiRSS116Parser}
 * @author delabre
 *
 */
public class Rss116Writer extends PmsiXmlPipedOutputStreamWriter {

	/**
	 * Construction du Writer avec son reader associé
	 * @throws PmsiWriterException 
	 */
	public Rss116Writer() throws PmsiWriterException {
		super("UTF-8");
	}
	
	/**
	 * Ajoute des données liées à une ligne pmsi
	 * @param lineType ligne avec les données à insérer
	 * @throws PmsiWriterException 
	 */
	public void writeLineElement(PmsiLineType lineType) throws PmsiWriterException  {
		// Lecture des données du header
		if (lineType instanceof PmsiRss116Header) {
			// Ecriture de la ligne header sans la fermer (va contenir les rss)
			super.writeLineElement(lineType);
		}
		
		// Lecture d'un RSS MainExample
		else if (lineType instanceof PmsiRss116Main) {
			// Si un rss main est ouvert, il faut le fermer
			if (getLastLine() instanceof PmsiRss116Main) {
				super.writeEndElement();
			}
			// ouverture du rss main
			super.writeLineElement(lineType);
		}
		
		// Lecture d'autres données
		else if (lineType instanceof PmsiRss116Acte || lineType instanceof PmsiRss116Da ||
				lineType instanceof PmsiRss116Dad) {
			// Ouverture de la ligne
			super.writeLineElement(lineType);
			// fermeture de la ligne
			super.writeEndElement();
		}
	}
}
