package aider.org.pmsi.dto;

import java.util.HashMap;

public class PmsiDtoReportImpl implements PmsiDtoReport {

	@Override
	public boolean getStatus() {
		return true;
	}

	@Override
	public HashMap<PmsiDtoReportError, Object> getReport() {
		return new HashMap<PmsiDtoReportError, Object>();
	}
	
}
