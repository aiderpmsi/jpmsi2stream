package aider.org.machinestate;

public class MachineStateException extends Exception {

	/**
	 * numéro de série autogénéré
	 */
	private static final long serialVersionUID = 6569320738496102794L;

	/**
	 * @see Exception#Exception()
	 */
	public MachineStateException() {}

	/**
	 * @see Exception#Exception(String)
	 * @param arg0
	 */
	public MachineStateException(String arg0) {
		super(arg0);
	}

	/**
	 * @see Exception#Exception(Throwable)
	 * @param arg0
	 */
	public MachineStateException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @see Exception#Exception(String, Throwable)
	 * @param arg0
	 * @param arg1
	 */
	public MachineStateException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
