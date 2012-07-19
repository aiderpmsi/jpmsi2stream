package aider.org.pmsi.dto;

import aider.org.pmsi.parser.PmsiReader;

public class PmsiDtoReportFactory {

	public PmsiDtoReport getPmsiDtoReport(PmsiReader<?, ?> reader) {
		return new PmsiDtoReportImpl();
	}
	
	public void close() {
	}
	
}
