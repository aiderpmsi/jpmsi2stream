package org.aider.pmsi2sql.machineState;

import java.util.HashMap;

public abstract class MachineState {

	/**
	 * L'état de départ est un int égal à 0
	 */
	public static final int STATE_READY = 0;
	public static final int STATE_FINISHED = -1;
	
	/**
	 * La table de changement d'état associe un signal
	 * à un chamgement d'état à partir d'un état vers un autre : 
	 * Le premier integer est le signal, le deuxième l'état initial
	 * et le troisième l'état final
	 */
	HashMap<Integer, HashMap<Integer, Integer>> statesTable;
	
	/**
	 * Indicateur définissant l'état actuel
	 */
	Integer etatActuel;
	
	/**
	 * Constructeur. La machine est initialisée avec un entier correspondant
	 * à START
	 */
	public MachineState() {
		statesTable = new HashMap<Integer, HashMap<Integer,Integer>>();
		etatActuel = STATE_READY;
	}
	
	/**
	 * Insertion d'une transition entre signal, état initial et état filan
	 * @param MySignal
	 * @param MyInitialState
	 * @param MyFinalState
	 */
	public void addTransition(int MySignal, int MyInitialState, int MyFinalState) {
		if (!statesTable.containsKey(MySignal)) {
			statesTable.put(MySignal, new HashMap<Integer, Integer>());
		}
		statesTable.get(MySignal).put(MyInitialState, MyFinalState);
	}
	
	/**
	 * Modification de l'état de la machine selon le signal donné
	 * @param Mysignal
	 * @return
	 */
	public int changeState(int Mysignal) {
		etatActuel = statesTable.get(Mysignal).get(etatActuel);
		if (etatActuel == null)
			throw new RuntimeException ("Signal indéterminé dans l'état actuel de la machine à états");
		return etatActuel;
	}
	
	/**
	 * Retourne l'état de la machine
	 * @return Etat de la machine
	 */
	public int getState() {
		return etatActuel;
	}
	
	public void run() throws Exception{
		while (etatActuel != STATE_FINISHED) {
			process();
		}
	}
	
	public abstract void process() throws Exception;
}
