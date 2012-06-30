package aider.org.pmsi.dto;

import ru.ispras.sedna.driver.DriverException;
import aider.org.pmsi.parser.linestypes.PmsiLineType;

public interface DTOPmsiLineType {

	public void start(String name);
	
	public void appendContent(PmsiLineType lineType);
	
	public void end();
	
	public void close() throws DriverException;	
}
