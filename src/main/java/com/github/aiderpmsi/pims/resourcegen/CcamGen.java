package com.github.aiderpmsi.pims.resourcegen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

/**
 * We find the list of CCAM in NX file from AMELI
 * 
 * @author jpc
 *
 */
public class CcamGen {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		File input = new File(args[0]);
		File output = new File("src/main/resources/grouper-classeacte.cfg");
		BufferedReader br = new BufferedReader(new FileReader(input));
		BufferedWriter bw = new BufferedWriter(new FileWriter(output));
		
		// ONE TYPE (ADC / ADE / ...) HAS MULTIPLE CCAM
		TreeMap<String, TreeSet<String>> classeacte = new TreeMap<>();
		String line = br.readLine();

		while ((line) != null) {
			// IF WE HAVE A LINE BEGINING WITH 1010101, IT MEANS WE HAVE ONE CCAM ACTE DESCRIPTION
			if (line.startsWith("1010101")) {
				line = findActeDef(br, classeacte, line);
			} else {
				line = br.readLine();
			}
		}

		// NOW WE WRITE THE LIST OF CCCAM FOR EACH ACTE TYPE
		for (Entry<String, TreeSet<String>> entryclasse : classeacte.entrySet()) {
			bw.write("01:" + entryclasse.getKey() + "\n");
			for (String ccamRead : entryclasse.getValue()) {
				bw.write("02:" + ccamRead + "\n");
			}
		}
		
		br.close();
		bw.close();
	}
	
	/**
	 * Finds the elements of an acte
	 * @param br read from
	 * @param bw write to
	 * @param line New line read, has to be processed again in main loop
	 * @return
	 * @throws IOException 
	 */
	private static String findActeDef(BufferedReader br, TreeMap<String, TreeSet<String>> classeacte, String line) throws IOException {
		// GETS THE ACTE NAME
		String acte = line.substring(7, 20).trim();

		String typeacte = null, maxdate = null;
		List<String> phases = null;
		
		while (true) {
			// READ NEXT LINE
			line = br.readLine();
			
			// IF WE HAVE A LINE 201 (NEW ACTIVITY) OR 199 (END OF CODE DEFINITION)
			// STORE THE RESULTS
			if ((line.startsWith("2010101") || line.startsWith("199")) && typeacte != null) {
				// CHECH IF THIS TYPE OF ACTE EXISTS
				TreeSet<String> listactes = classeacte.get(typeacte);
				if (listactes == null) {
					listactes = new TreeSet<>();
					classeacte.put(typeacte, listactes);
				}
				// ADDS THE LIST OF COUPLES ACTE / PHASE
				for (String phase : phases) {
					listactes.add(acte + "/" + phase);
				}
			}
			
			// IF WE HAVE A LINE 201 (NEW ACTIVITY), RESET THE CURRENT DATAS AND CONTINUE
			if (line.startsWith("2010101")) {
				typeacte = null; maxdate = null; phases = new LinkedList<>();
			}
			
			// IF WE HAVE A LINE 199, READ NEXT LINE AND RETURN IT TO PROCESS
			if (line.startsWith("199")) {
				if ((line = br.readLine()) != null)
					return line;
				else
					throw new IOException("Malformed file : no 199 line after 201 line");
			}
			
			// IF WE HAVE A 2100101 LINE, GETS THE DATE AND TYPE AND COMPARE IT WITH THE CURRENT DATE AND TYPE
			if (line.startsWith("2100101")) {
				String thisDate = line.substring(7, 15);
				if (maxdate == null || maxdate.compareTo(thisDate) < 0) {
					maxdate = thisDate;
					typeacte = line.substring(63, 67).trim();
				}
			}

			// IF WE HAVE A 3010101 LINE, ADDS THIS POSSIBLE PHASE TO THE LIST OF PHASES
			if (line.startsWith("3010101")) {
				phases.add(line.substring(7, 8));
			}

		}
		
	}
}
