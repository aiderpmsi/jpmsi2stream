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
 * We find the list of excluded DP in volume_1.pdf
 * (pdftotext volume_1.pdf -nopgbrk -raw -f 410 -l 411)
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
				+ "(?:CMD\\d{2})|"
				+ "(?:Sous_CMD\\d{2}_(?:C|K|M))|"
				+ "(?:Racines_en_(?:C|K|M))"
				+ "))+)$");
		
		File input = new File(args[0]);
		File output = new File("src/main/resources/grouper-cma-exc-gh.cfg");
		BufferedReader br = new BufferedReader(new FileReader(input));
		BufferedWriter bw = new BufferedWriter(new FileWriter(output));
		
		String line;

		while ((line = br.readLine()) != null) {
			Matcher matcher = linepat.matcher(line);
			// WE HAVE ONE LIST OF GH EXCLUDING THIS CMA
			if (matcher.matches()) {
				bw.write("01:" + matcher.group(1) + "\n");
				// THEN CHECK EACH GHM ELEMENT
				String[] group = matcher.group(2).trim().split("\\s+");
				for (String element : group) {
					if (element.startsWith("Racines_en_")) {
						// 02 : RACINES WITH LETTER
						bw.write("02:" + element.replace("Racines_en_", "") + "\n");
					} else if (element.startsWith("Sous_CMD")) {
						// 03 : PART OF CMD
						bw.write("03:" + element.replace("Sous_CMD", "") + "\n");
					} else if (element.startsWith("CMD")) {
						// 04 : ONE CMD
						bw.write("04:" + element.replace("CMD", "") + "\n");
					} else {
						// 05 : ONE GHM
						bw.write("05:" + element + "\n");
					}
				}
			}
		}
		
		br.close();
		bw.close();
	}

}
