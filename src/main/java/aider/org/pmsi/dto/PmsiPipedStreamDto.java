package aider.org.pmsi.dto;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import aider.org.pmsi.dto.PmsiDto;
import aider.org.pmsi.exceptions.PmsiDtoException;

/**
 * Classe de transfert d'objet utilisant un {@link PipedInputStream} pour le connecter
 * à un {@link PipedOutputStream} afin d'envoyer les données dans un autre container 
 * @author delabre
 *
 */
public abstract class PmsiPipedStreamDto implements PmsiDto {

	private PipedInputStream inputStream;
		
	public PmsiPipedStreamDto() {
		this.inputStream = new PipedInputStream();
	}
	
	public PipedInputStream getPipedInputStream() {
		return inputStream;
	}

	@Override
	public void close() throws PmsiDtoException {
		try {
			inputStream.close();
		} catch (IOException e) {
			throw new PmsiDtoException(e);
		}
	}
}
