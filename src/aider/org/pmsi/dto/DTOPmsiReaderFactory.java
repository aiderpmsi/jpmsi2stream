package aider.org.pmsi.dto;

import java.io.IOException;

import ru.ispras.sedna.driver.DatabaseManager;
import ru.ispras.sedna.driver.DriverException;
import ru.ispras.sedna.driver.SednaConnection;

import aider.org.pmsi.parser.PmsiReader;
import aider.org.pmsi.reader.PmsiRSF2009Reader;
import aider.org.pmsi.reader.PmsiRSF2012Reader;
import aider.org.pmsi.reader.PmsiRSS116Reader;

public class DTOPmsiReaderFactory {

	private SednaConnection connection = null;
	
	public DTOPmsiReaderFactory() throws DriverException {
		connection = DatabaseManager.getConnection(
				"localhost:5050",
				"testdb",
				"SYSTEM",
				"PASSWORD");
	}
	
	public DTOPmsiLineType getDtoPmsiLineType(PmsiReader<?, ?> reader) throws DriverException, IOException, InterruptedException {
		if (reader instanceof PmsiRSF2009Reader) {
			return new DtoRsf2009(connection);
		} else if (reader instanceof PmsiRSF2012Reader) {
			return new DtoRsf2012(connection);
		} else if (reader instanceof PmsiRSS116Reader) {
			return new DtoRss116(connection);
		}
		return null;
	}
	
	public void close() throws DriverException {
		if (connection != null) {
			connection.close();
			connection = null;
		}
	}
}
