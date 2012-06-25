package aider.org.pmsi.dto;

import java.io.File;
import java.io.FileNotFoundException;

import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.Environment;
import com.sleepycat.db.EnvironmentConfig;
import com.sleepycat.dbxml.XmlContainerConfig;
import com.sleepycat.dbxml.XmlManagerConfig;

import aider.org.pmsi.parser.PmsiReader;
import aider.org.pmsi.reader.PmsiRSF2009Reader;

public class DTOPmsiReaderFactory {

	private Environment dbEnvironment = null;
	
	private XmlManagerConfig xmlManagerConfig = null;
	
	private XmlContainerConfig containerConf = null;
	
	public DTOPmsiReaderFactory() throws FileNotFoundException, DatabaseException {
		// Choix de l'environnement de berkeley database
		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setAllowCreate(true);
		envConfig.setInitializeCache(true);
		envConfig.setInitializeLocking(true);
	    envConfig.setInitializeLogging(true);
	    envConfig.setTransactional(true);    
		
	    dbEnvironment = new Environment(new File(System.getProperty("user.dir") + File.separator + "db"), envConfig);

	    // Choix de la configuration de berkeley xml database
	    xmlManagerConfig = new XmlManagerConfig();
	    xmlManagerConfig.setAdoptEnvironment(true);
	    
	    // Choix de la configuration des containers
	    containerConf = new XmlContainerConfig();
	    containerConf.setTransactional(true);
	    containerConf.setAllowCreate(true);
	}
	
	public DTOPmsiLineType getDtoPmsiLineType(PmsiReader<?, ?> reader) throws FileNotFoundException, DatabaseException {
		if (reader instanceof PmsiRSF2009Reader) {
			return new DtoRsf2009(dbEnvironment, xmlManagerConfig, containerConf);
		}
		return null;
	}
}
