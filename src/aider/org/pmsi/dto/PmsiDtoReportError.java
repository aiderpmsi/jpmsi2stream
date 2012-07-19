package aider.org.pmsi.dto;

public class PmsiDtoReportError {

	public enum Origin {
		DTO_REPORT, DTO, PMSI_READER, PMSI_WRITER, PMSI_PARSER
	}
	
	private String name;
	
	private Origin origin;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Origin getOrigin() {
		return origin;
	}

	public void setOrigin(Origin origin) {
		this.origin = origin;
	}
	
	
	
}
