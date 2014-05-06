package com.github.aiderpmsi.pims.resourcegen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * We find the list of excluded DP in sts_20130005_0001_p000.pdf pages 384 to 432
 * (pdftotext sts_20130005_0001_p000.pdf -f 384 -l 432)
 * 
 * @author jpc
 *
 */
public class CmaExcludeDP {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		Pattern dpexcpat = Pattern.compile("^\\d+");
		
		File input = new File(args[0]);
		File output = new File("src/main/resources/cma-exc-dp.xml");
		BufferedReader br = new BufferedReader(new FileReader(input));
		BufferedWriter bw = new BufferedWriter(new FileWriter(output));
		
		// START THE XML
		
		bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		bw.write("<dp-exclude-cmas>\n");
		
		String line, dpexcnumber;

		while ((line = br.readLine()) != null) {
			Matcher matcher = dpexcpat.matcher(line);
			// WE HAVE ONE LIST OF DP EXCLUDING THIS CMA
			if (matcher.matches()) {
				dpexcnumber = matcher.group();
				bw.write("    <excluding id=\"CMA-DP-" + dpexcnumber + "\" >\n");
				// NEXT LINE IS VOID
				br.readLine();
				// NOW, WHILE LINE IS NOT VOID, GET THE LIST OF EXCLUDED DP'S
				while ((line = br.readLine()) != null && line.trim().length() != 0) {
					// SPLIT THE LINE INTO DPS
					String[] chunks = line.split(" ");
					for (String chunk : chunks) {
						if (chunk.contains("-")) {
							// IF WE HAVE A START AND END DP, STORE AS A GROUP
							bw.write("        <group>\n");
							String[] startstop = chunk.split("-");
							bw.write("             <first>" + startstop[0] + "</first>\n");
							bw.write("             <last>" + startstop[1] + "</last>\n");
							bw.write("        </group>\n");
						} else {
							// ITS A SINGLE DP (OR ROOT CIM)
							bw.write("        <element>" + chunk + "</element>\n");
						}
					}
				}
				bw.write("    </excluding>\n");
			}
		}
		
		// FINISHES THE EXCLUDED DPS
		bw.write("<dp-exclude-cmas>\n");
		
		br.close();
		bw.close();
	}

}
