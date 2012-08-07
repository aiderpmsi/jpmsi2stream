package aider.org.machinestate;

public class MockMachineState extends
		MachineState<MockMachineState.EnumState, MockMachineState.EnumSignal, String> {

	/**
	 * Liste des états de la machine à états
	 * @author delabre
	 *
	 */
	public enum EnumState {
		STATE_READY, STATE_FINISHED, STATE_MIDDLE
	}
	
	/**
	 * Liste des signaux
	 * @author delabre
	 *
	 */
	public enum EnumSignal {
		SIGNAL_START, // STATE_READY -> STATE_MIDDLE
		SIGNAL_FINISH, // STATE_MIDDLE -> STATE_FINISHED
	}
	
	public MockMachineState() {
		super(EnumState.STATE_READY, EnumState.STATE_FINISHED);
		
		// Définition des états et des signaux de la machine à états
		addTransition(EnumSignal.SIGNAL_START, EnumState.STATE_READY, EnumState.STATE_MIDDLE);
		addTransition(EnumSignal.SIGNAL_FINISH, EnumState.STATE_MIDDLE, EnumState.STATE_FINISHED);
	}

	@Override
	protected void process() throws MachineStateException {
		switch (getState()) {
		case STATE_READY:
			changeState(EnumSignal.SIGNAL_START);
			break;
		case STATE_MIDDLE:
			changeState(EnumSignal.SIGNAL_FINISH);
			break;
		}
	}
	
}
