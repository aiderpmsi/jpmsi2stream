package aider.org.pmsi.dto;

import java.io.InputStream;
import java.util.HashMap;

import aider.org.pmsi.parser.exceptions.PmsiPipedIOException;

public class PmsiDtoImpl implements PmsiDto {
	
	PmsiDtoReport pmsiDtoReport;
	
	public PmsiDtoImpl(PmsiDtoReport pmsiDtoReport) {
		this.pmsiDtoReport = pmsiDtoReport;
	}
	
	public void writePmsi(InputStream input) throws PmsiPipedIOException {
		try {
			byte buffer[] = new byte[512];
			int size;
			while ((size = input.read(buffer)) != -1) {
				System.out.println(new String(buffer, 0, size));
				// Vérification que le thread n'a pas été interrompu
				if (Thread.currentThread().isInterrupted())
					throw new InterruptedException("Thread interrompu");
			}
		} catch (Exception e) {
			throw new PmsiPipedIOException(e);
		}
	}
	
	public void close() throws PmsiPipedIOException {
		// Do nothing
	}

	@Override
	public HashMap<PmsiDtoReportError, Object> getReport() {
		return pmsiDtoReport.getReport();
	}

	@Override
	public boolean getStatus() {
		return getStatus();
	}
	
}
