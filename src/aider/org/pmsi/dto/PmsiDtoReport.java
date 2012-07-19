package aider.org.pmsi.dto;

import java.util.HashMap;

public interface PmsiDtoReport {

	public boolean getStatus();
	
	public HashMap<PmsiDtoReportError, Object> getReport();
}
