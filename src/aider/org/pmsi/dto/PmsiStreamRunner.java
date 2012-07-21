package aider.org.pmsi.dto;

import java.io.IOException;
import java.io.InputStream;

import aider.org.pmsi.parser.exceptions.PmsiRunnableException;

public class PmsiStreamRunner implements PmsiRunnable {

	private InputStream inputStream;
	
	public PmsiStreamRunner(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	@Override
	public void run() throws PmsiRunnableException {
		byte buffer[] = new byte[512];
		int size;
		
		try {
			while ((size = inputStream.read(buffer)) != -1) {
				System.out.println(new String(buffer, 0, size));
				// Vérification que le thread n'a pas été interrompu
				if (Thread.currentThread().isInterrupted())
					throw new PmsiRunnableException(new InterruptedException("Thread interrompu"));
			}
		} catch (IOException e) {
			throw new PmsiRunnableException(e);
		}
	}

	@Override
	public void close() {
	}
}
