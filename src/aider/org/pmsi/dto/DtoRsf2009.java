package aider.org.pmsi.dto;

import java.io.FileNotFoundException;
import aider.org.pmsi.parser.linestypes.PmsiLineType;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009Header;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009a;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009b;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009c;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009h;
import aider.org.pmsi.parser.linestypes.PmsiRsf2009m;

import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.Environment;
import com.sleepycat.dbxml.XmlContainerConfig;
import com.sleepycat.dbxml.XmlException;
import com.sleepycat.dbxml.XmlManagerConfig;

public class DtoRsf2009 extends DtoRsf {

	/**
	 * Construction de la connexion à la base de données à partir des configurations
	 * données
	 * @param dbEnvironment
	 * @param xmlManagerConfig
	 * @param xmlContainerConfig
	 * @throws FileNotFoundException
	 * @throws DatabaseException
	 */
	public DtoRsf2009(Environment dbEnvironment,
			XmlManagerConfig xmlManagerConfig,
			XmlContainerConfig xmlContainerConfig) throws FileNotFoundException, DatabaseException {
		super(dbEnvironment, xmlManagerConfig, xmlContainerConfig);
	}
	
	/**
	 * Ajoute des données liées à une ligne pmsi
	 * @param lineType ligne avec les données à insérer
	 */
	public void appendContent(PmsiLineType lineType) throws XmlException  {
		if (document == null)
			throw new RuntimeException("Document not initialized");
		
		if (lineType instanceof PmsiRsf2009Header) {
			// Ecriture de la ligne header sans la fermer (va contenir les rsf)
			writeSimpleElement(lineType);
			// Prise en compte de l'ouverture de la ligne
			lastLine.add(lineType);
		} else if (lineType instanceof PmsiRsf2009a) {
			// Si un rsfa est ouvert, il faut le fermer
			if (lastLine.lastElement() instanceof PmsiRsf2009a) {
				writer.writeEndElement(lastLine.pop().getName(), null, null);
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
			writer.writeEndElement(lineType.getName(), null, null);
		}
	}
}
