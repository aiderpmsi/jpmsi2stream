/**
 * <p>
 * Package gérant l'écriture des données xml dans un pipedwiter
 * Les données sont ensuite récupérées par un pipedreader qui en fait ce qu'il veut.
 * </p>
 * 
 * <p>
 * Le schéma est le suivant :
 * <br/>
 * aider.org.pmsi.parser.PmsiParser &#8658;
 * aider.org.pmsi.dto.PmsiPipedWriter &#8658;
 * aider.org.pmsi.dto.PmsiThreadedPipedReader &#8658;
 * aider.org.pmsi.dto.PmsiDto &#8658;
 * aider.org.pmsi.dto.PmsiDtoReport
 * </p>
 * 
 * <p>
 * Le construction de ces classes (sauf PmsiParser) est gérée par des fabirques imbriquées. :
 * <ol>
 *   <li>On crée un PmsiDtoReportFactory sans avoir besoin d'une autre classe</li>
 *   <li>On crée un PmsiDtoFactory en donnant au constructeur un PmsiDtoReportFactory</li>
 *   <li>On crée un PmsiThreadedPipedReaderFactory en donnant au constructeur un PmsiDtoFactory</li>
 *   <li>On crée un PmsiPipedWriterFactory en donnant au constructeur un PmsiThreadedPipedReaderFactory</li>
 * </ol>
 * 
 * </p>
 */

package aider.org.pmsi.writer;