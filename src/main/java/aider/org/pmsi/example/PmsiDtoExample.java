package aider.org.pmsi.example;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PrintStream;

import aider.org.pmsi.dto.PmsiDto;
import aider.org.pmsi.exceptions.PmsiDtoRunnableException;

public class PmsiDtoExample implements PmsiDto {

	private PipedInputStream inputStream;
	
	private PrintStream outputStream;
	
	public PmsiDtoExample(OutputStream outputStream) {
		this.inputStream = new PipedInputStream();
		this.outputStream = new PrintStream(outputStream);
	}
	
	public PipedInputStream getPipedInputStream() {
		return inputStream;
	}
	
	@Override
	public void transfer() throws PmsiDtoRunnableException {
		byte buffer[] = new byte[512];
		int size;
		
		try {
			while ((size = inputStream.read(buffer)) != -1) {
				outputStream.println(new String(buffer, 0, size));
				// Vérification que le thread n'a pas été interrompu
				if (Thread.currentThread().isInterrupted())
					throw new PmsiDtoRunnableException(new InterruptedException("Thread interrompu"));
			}
		} catch (IOException e) {
			throw new PmsiDtoRunnableException(e);
		}
	}

	@Override
	public void close() throws PmsiDtoRunnableException {
		try {
			inputStream.close();
		} catch (IOException e) {
			throw new PmsiDtoRunnableException(e);
		}
	}
}
