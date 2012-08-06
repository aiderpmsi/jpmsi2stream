package aider.org.pmsi.dto;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import aider.org.pmsi.exceptions.PmsiMuxerException;

public class PmsiStreamMuxerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Teste la création et la destuction du {@link PmsiStreamMuxer}
	 * @throws PmsiMuxerException 
	 */
	@Test
	public void testOnOff() throws PmsiMuxerException {
		PmsiStreamMuxer pmsiStreamMuxer = new PmsiStreamMuxer();
		
		pmsiStreamMuxer.close();
		
		assertEquals("Stream Muxer non correctement fermé", true, pmsiStreamMuxer.isClosed());
	}

}
