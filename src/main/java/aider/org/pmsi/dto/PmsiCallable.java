package aider.org.pmsi.dto;

import java.util.concurrent.Callable;

/**
 * Classe permettant de réaliser un traitement et de renvoyer un résultat à la classe
 * parente
 * @author delabre
 *
 */
public class PmsiCallable<ReturnType> implements Callable<ReturnType> {

	/**
	 * Classe implémentant {@link PmsiDto} à lancer
	 */
	private PmsiDto<ReturnType> pmsiDto;
		
	/**
	 * Constructeur
	 * @param pmsiDto sera lancée lors de l'exécution de {@link PmsiCallable#pmsiDto}
	 */
	public PmsiCallable(PmsiDto<ReturnType> pmsiDto) {
		this.pmsiDto = pmsiDto;
	}

	@Override
	public ReturnType call() throws Exception {
		pmsiDto.transfer();
		return pmsiDto.getEndMessage();
	}
	
}
