package aider.org.pmsi.example;

import java.io.IOException;
import java.io.InputStream;

import aider.org.pmsi.dto.PmsiDtoRunnable;
import aider.org.pmsi.exceptions.PmsiDtoRunnableException;

public class PmsiDtoRunner implements PmsiDtoRunnable {

	private InputStream inputStream;
	
	public PmsiDtoRunner(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	@Override
	public void run() throws PmsiDtoRunnableException {
		byte buffer[] = new byte[512];
		int size;
		
		try {
			while ((size = inputStream.read(buffer)) != -1) {
				System.out.println(new String(buffer, 0, size));
				// Vérification que le thread n'a pas été interrompu
				if (Thread.currentThread().isInterrupted())
					throw new PmsiDtoRunnableException(new InterruptedException("Thread interrompu"));
			}
		} catch (IOException e) {
			throw new PmsiDtoRunnableException(e);
		}
	}

	@Override
	public void close() {
	}
}
