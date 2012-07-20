package aider.org.pmsi.dto;

import java.io.InputStream;

public class PmsiStreamRunner implements PmsiRunnable {

	private InputStream inputStream;
	
	private InsertionReport report;
	
	public PmsiStreamRunner(InputStream inputStream, InsertionReport report) {
		this.inputStream = inputStream;
		this.report = report;
	}
	
	@Override
	public void run() throws Exception {
		byte buffer[] = new byte[512];
		int size;
		while ((size = inputStream.read(buffer)) != -1) {
			System.out.println(new String(buffer, 0, size));
			// Vérification que le thread n'a pas été interrompu
			if (Thread.currentThread().isInterrupted())
				throw new InterruptedException("Thread interrompu");
		}
		report.setReaderSuccess(true);
	}

}
