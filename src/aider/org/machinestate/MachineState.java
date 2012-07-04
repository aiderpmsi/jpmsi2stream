package aider.org.machinestate;

import java.util.HashMap;

public abstract class MachineState<EnumState, EnumSignal> {

	private EnumState stateFinished;
	
	/**
	 * La table de changement d'état associe un signal
	 * à un chamgement d'état à partir d'un état initial :
	 */
	HashMap<EnumSignal, HashMap<EnumState, EnumState>> transitionsTable =
			new HashMap<EnumSignal, HashMap<EnumState,EnumState>>();
	
	/**
	 * Indicateur définissant l'état actuel
	 */
	private EnumState stateActual;
	
	protected MachineState() {}
	
	public MachineState(EnumState stateReady, EnumState stateFinished) {
		this.stateActual = stateReady;
		this.stateFinished = stateFinished;
	}
	
	/**
	 * Insertion d'une transition entre signal, état initial et état final
	 * @param signal
	 * @param stateInitial
	 * @param stateFinal
	 */
	public void addTransition(EnumSignal signal, EnumState stateInitial, EnumState stateFinal) {
		if (!transitionsTable.containsKey(signal)) {
			transitionsTable.put(signal, new HashMap<EnumState, EnumState>());
		}
		transitionsTable.get(signal).put(stateInitial, stateFinal);
	}

	/**
	 * Modification de l'état de la machine selon le signal donné
	 * @param signal
	 * @return Le nouvel état de la machine à états
	 */
	public EnumState changeState(EnumSignal signal) {
		EnumState newState = transitionsTable.get(signal).get(stateActual);
		if (newState == null)
			throw new RuntimeException ("Signal " + signal.toString() + " indéterminé dans l'état actuel " + stateActual.toString() + " de la machine à états");
		else
			stateActual = newState; 
		return stateActual;
	}
	
	/**
	 * Retourne l'état de la machine
	 * @return Etat de la machine
	 */
	public EnumState getState() {
		return stateActual;
	}
	
	/**
	 * Lancement de la machine à états
	 * @throws Exception
	 */
	public void run() throws Exception {
		while (stateActual != stateFinished) {
			process();
		}
		finish();
	}
	
	/**
	 * Méthode à implémenter pour lancer la machine à états
	 * @throws Exception
	 */
	public abstract void process() throws Exception;
	
	/**
	 * Méthode à implémenter pour réaliser une action à la fin du fonctionnement de
	 * la machine à états
	 * @throws Exception
	 */
	public abstract void finish() throws Exception;
}
