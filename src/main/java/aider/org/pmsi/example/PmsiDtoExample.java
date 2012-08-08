package aider.org.pmsi.example;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import aider.org.pmsi.dto.PmsiPipedStreamDto;
import aider.org.pmsi.exceptions.PmsiDtoException;

public class PmsiDtoExample extends PmsiPipedStreamDto<String> {
	
	private PrintStream outputStream;
	
	public PmsiDtoExample(OutputStream outputStream) {
		super();
		this.outputStream = new PrintStream(outputStream);
	}
	
	@Override
	public void transfer() throws PmsiDtoException {
		byte buffer[] = new byte[512];
		int size;
		
		try {
			while ((size = getPipedInputStream().read(buffer)) != -1) {
				outputStream.println(new String(buffer, 0, size));
				// Vérification que le thread n'a pas été interrompu
				if (Thread.currentThread().isInterrupted())
					throw new PmsiDtoException(new InterruptedException("Thread interrompu"));
			}
		} catch (IOException e) {
			throw new PmsiDtoException(e);
		}
	}

	@Override
	public void close() throws PmsiDtoException {
		super.close();
	}

	@Override
	public String getEndMessage() throws PmsiDtoException {
		return "PmsiDtoExample done";
	}
}
