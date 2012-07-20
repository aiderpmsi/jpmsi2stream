package aider.org.pmsi.writer;

import java.io.OutputStream;

import aider.org.pmsi.parser.PmsiRSF2012Reader;
import aider.org.pmsi.parser.exceptions.PmsiWriterException;
import aider.org.pmsi.parser.linestypes.PmsiLineType;
import aider.org.pmsi.parser.linestypes.PmsiRsf2012Header;
import aider.org.pmsi.parser.linestypes.PmsiRsf2012a;
import aider.org.pmsi.parser.linestypes.PmsiRsf2012b;
import aider.org.pmsi.parser.linestypes.PmsiRsf2012c;
import aider.org.pmsi.parser.linestypes.PmsiRsf2012h;
import aider.org.pmsi.parser.linestypes.PmsiRsf2012l;
import aider.org.pmsi.parser.linestypes.PmsiRsf2012m;

/**
 * Writer {@link PmsiWriterImpl} pour {@link PmsiRSF2012Reader}
 * @author delabre
 *
 */
public class Rsf2012Writer extends PmsiWriterImpl {

	/**
	 * Construction du Writer avec son reader associé
	 * @throws PmsiWriterException 
	 */
	public Rsf2012Writer(OutputStream outputStream) throws PmsiWriterException {
		super(outputStream, "UTF-8");
	}
	
	/**
	 * Ajoute des données liées à une ligne pmsi
	 * @param lineType ligne avec les données à insérer
	 * @throws PmsiWriterException 
	 */
	public void writeLineElement(PmsiLineType lineType) throws PmsiWriterException  {
		// Header
		if (lineType instanceof PmsiRsf2012Header) {
			// Ecriture de la ligne header sans la fermer (va contenir les rsf)
			super.writeLineElement(lineType);
		}
		
		// RSFA
		else if (lineType instanceof PmsiRsf2012a) {
			// Si un rsfa est ouvert, il faut le fermer
			if (getLastLine() instanceof PmsiRsf2012a) {
				super.writeEndElement();
			}
			// ouverture du rsfa
			super.writeLineElement(lineType);

		}
		
		// Autres lignes
		else if (lineType instanceof PmsiRsf2012b || lineType instanceof PmsiRsf2012c ||
				lineType instanceof PmsiRsf2012h || lineType instanceof PmsiRsf2012m ||
				lineType instanceof PmsiRsf2012l) {
			// Ouverture de la ligne
			super.writeLineElement(lineType);
			// fermeture de la ligne
			super.writeEndElement();
		}

	}
}
