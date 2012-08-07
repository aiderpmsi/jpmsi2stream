package aider.org.machinestate;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MachineStateTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIsolated() throws Exception {
		MockMachineState mms = new MockMachineState();
		
		assertNull(mms.call());
	}

}
