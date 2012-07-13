package aider.org.pmsi.dto;

import java.io.FileNotFoundException;
import java.io.IOException;

import ru.ispras.sedna.driver.DriverException;
import ru.ispras.sedna.driver.SednaConnection;
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
	 * @throws DriverException 
	 * @throws InterruptedException 
	 * @throws FileNotFoundException
	 */
	public DtoRsf2009(SednaConnection connection) throws DriverException, IOException, InterruptedException {
		super(connection);
	}
	
	/**
	 * Ajoute des données liées à une ligne pmsi
	 * @param lineType ligne avec les données à insérer
	 */
	public void appendContent(PmsiLineType lineType)  {
		if (lineType instanceof PmsiRsf2009Header) {
			// Ecriture de la ligne header sans la fermer (va contenir les rsf)
			writeSimpleElement(lineType);
			// Prise en compte de l'ouverture de la ligne
			lastLine.add(lineType);
		} else if (lineType instanceof PmsiRsf2009a) {
			// Si un rsfa est ouvert, il faut le fermer
			if (lastLine.lastElement() instanceof PmsiRsf2009a) {
				out.println("</" + lastLine.pop().getName() + ">");
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
			out.println("</" + lineType.getName() + ">");
		}
	}
}
