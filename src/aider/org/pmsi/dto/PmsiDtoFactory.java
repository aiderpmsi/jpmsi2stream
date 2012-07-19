package aider.org.pmsi.dto;

import aider.org.pmsi.parser.PmsiReader;

public class PmsiDtoFactory {

	private PmsiDtoReportFactory pmsiDtoReportFactory;
	
	public PmsiDtoFactory(PmsiDtoReportFactory pmsiDtoReportFactory) {
		this.pmsiDtoReportFactory = pmsiDtoReportFactory;
	}
	
	public PmsiDto getPmsiDto(PmsiReader<?, ?> reader) {
		PmsiDtoReport pmsiDtoReport = pmsiDtoReportFactory.getPmsiDtoReport(reader);
		return new PmsiDtoImpl(pmsiDtoReport);
	}
	
	public void close() {
	}
	
}
