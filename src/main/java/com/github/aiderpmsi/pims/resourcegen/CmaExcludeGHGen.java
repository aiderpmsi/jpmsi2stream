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
 * We find the list of excluded DP in sts_20130005_0001_p000.pdf
 * (pdftotext sts_20130005_0001_p000.pdf -nopgbrk -raw -f 433 -l 434)
 * 
 * @author jpc
 *
 */
public class CmaExcludeGHGen {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		Pattern linepat = Pattern.compile(
				"^(\\d+)"
				+ "((?:"
				+ "\\s+"
				+ "(?:"
				+ "(\\d{2}[A-Z]\\d{2})|"
				+ "(?:(?:Sous_CMD_)|(?:CMD)\\d{2})|"
				+ "(?:Racines_en_(?:C|K|M))"
				+ "))+)$");
		
		File input = new File(args[0]);
		File output = new File("src/main/resources/grouper-cma-exc-gh.xml");
		BufferedReader br = new BufferedReader(new FileReader(input));
		BufferedWriter bw = new BufferedWriter(new FileWriter(output));
		
		// START THE XML
		
		bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		bw.write("<gh-exclude-cmas>\n");
		
		String line;

		while ((line = br.readLine()) != null) {
			Matcher matcher = linepat.matcher(line);
			// WE HAVE ONE LIST OF GH EXCLUDING THIS CMA
			if (matcher.matches()) {
				bw.write("    <excluding id=\"CMA-GH-" + matcher.group(1) + "\" >\n");
				// THEN CHECK EACH GHM ELEMENT
				String[] group = matcher.group(2).split(" ");
				for (String element : group) {
					if (element.length() == 0) {
						continue;
					} else if (element.startsWith("Racines_en_")) {
						bw.write("        <racine>" + element.replace("Racines_en_", "") + "</racine>\n");
					} else if (element.startsWith("Sous_CMD_")) {
						bw.write("        <cmd>" + element.replace("Sous_CMD_", "") + "</cmd>\n");
					} else if (element.startsWith("CMD")) {
						bw.write("        <cmd>" + element.replace("CMD", "") + "</cmd>\n");
					} else {
						bw.write("        <ghm>" + element + "</ghm>\n");
					}
					bw.write("    </excluding>\n");
				}
			}
		}
		
		// FINISHES THE EXCLUDED DPS
		bw.write("<gh-exclude-cmas>\n");
		
		br.close();
		bw.close();
	}

}
