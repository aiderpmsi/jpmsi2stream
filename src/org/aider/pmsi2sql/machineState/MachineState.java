package org.aider.pmsi2sql.machineState;

import java.util.HashMap;

public abstract class MachineState {

	/**
	 * L'�tat de d�part est un int �gal � 0
	 */
	public static final int STATE_READY = 0;
	public static final int STATE_FINISHED = -1;
	
	/**
	 * La table de changement d'�tat associe un signal
	 * � un chamgement d'�tat � partir d'un �tat vers un autre : 
	 * Le premier integer est le signal, le deuxi�me l'�tat initial
	 * et le troisi�me l'�tat final
	 */
	HashMap<Integer, HashMap<Integer, Integer>> statesTable;
	
	/**
	 * Indicateur d�finissant l'�tat actuel
	 */
	Integer etatActuel;
	
	/**
	 * Constructeur. La machine est initialis�e avec un entier correspondant
	 * � START
	 */
	public MachineState() {
		statesTable = new HashMap<Integer, HashMap<Integer,Integer>>();
		etatActuel = STATE_READY;
	}
	
	/**
	 * Insertion d'une transition entre signal, �tat initial et �tat filan
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
	 * Modification de l'�tat de la machine selon le signal donn�
	 * @param Mysignal
	 * @return
	 */
	public int changeState(int Mysignal) {
		etatActuel = statesTable.get(Mysignal).get(etatActuel);
		if (etatActuel == null)
			throw new RuntimeException ("Signal ind�termin� dans l'�tat actuel de la machine � �tats");
		return etatActuel;
	}
	
	/**
	 * Retourne l'�tat de la machine
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
