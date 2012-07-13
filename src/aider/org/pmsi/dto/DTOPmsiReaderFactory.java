package aider.org.pmsi.dto;

import ru.ispras.sedna.driver.DriverException;
import aider.org.pmsi.parser.PmsiReader;
import aider.org.pmsi.reader.PmsiRSF2009Reader;
import aider.org.pmsi.reader.PmsiRSF2012Reader;
import aider.org.pmsi.reader.PmsiRSS116Reader;

public class DTOPmsiReaderFactory {
	
	public DTOPmsiReaderFactory() throws DriverException {
	}
	
	public DtoPmsiLineType getDtoPmsiLineType(PmsiReader<?, ?> reader) throws DtoPmsiException {
		if (reader instanceof PmsiRSF2009Reader) {
			return new DtoRsf2009();
		} else if (reader instanceof PmsiRSF2012Reader) {
			return new DtoRsf2012();
		} else if (reader instanceof PmsiRSS116Reader) {
			return new DtoRss116();
		}
		return null;
	}
	
	public void close() {
	}
}
