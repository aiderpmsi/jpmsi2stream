package aider.org.pmsi.writer;

import java.io.OutputStream;

import aider.org.pmsi.exceptions.PmsiWriterException;
import aider.org.pmsi.parser.PmsiRSF2009Reader;
import aider.org.pmsi.parser.linestypes.PmsiLineType;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009Header;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009a;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009b;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009c;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009h;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009m;

/**
 * Writer {@link PmsiWriterImpl} pour {@link PmsiRSF2009Reader}
 * @author delabre
 *
 */
public class Rsf2009Writer extends PmsiWriterImpl {

	/**
	 * Construction du Writer avec son reader associé
	 * @throws PmsiWriterException 
	 */
	public Rsf2009Writer(OutputStream outputStream) throws PmsiWriterException {
		super(outputStream, "UTF-8");
	}
	
	/**
	 * Ajoute des données liées à une ligne pmsi
	 * @param lineType ligne avec les données à insérer
	 * @throws PmsiWriterException 
	 */
	public void writeLineElement(PmsiLineType lineType) throws PmsiWriterException {
		// Header
		if (lineType instanceof PmsiRsf2009Header) {
			// Ecriture de la ligne header sans la fermer (va contenir les rsf)
			super.writeLineElement(lineType);
		}
		
		// Ligne RSFA
		else if (lineType instanceof PmsiRsf2009a) {
			// Si un rsfa est ouvert, il faut d'abord le fermer
			if (getLastLine() instanceof PmsiRsf2009a) {
				super.writeEndElement();
			}
			// ouverture du nouveau rsfa
			super.writeLineElement(lineType);
		}
		
		// Autres lignes
		else if (lineType instanceof PmsiRsf2009b || lineType instanceof PmsiRsf2009c ||
				lineType instanceof PmsiRsf2009h || lineType instanceof PmsiRsf2009m) {
			// Ouverture de la ligne
			super.writeLineElement(lineType);
			// fermeture de la ligne
			super.writeEndElement();
		}
	}
}
