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
 * We find the lists in sts_20130005_0002_p000.pdf
 * (pdftotext sts_20130005_0002_p000.pdf -nopgbrk -raw)
 * 
 * @author jpc
 *
 */
public class UnclassifiedGen {

	private static Pattern diagEntree = Pattern.compile("^Diagnostics d'entrée dans la CMD n° (\\d+)");  // WE DO NOT USE THIS LIST OF DP'S (WE USE THE ONE IN VOLUME 1)
	private static Pattern listeEntree = Pattern.compile("^Liste ([A-Z]-\\d+).*");
	private static Pattern cim = Pattern.compile("^([A-Z]\\d+(?:\\.\\d+(?:\\+\\d+)?)?).*");
	private static Pattern ccam = Pattern.compile("^([A-Z]{4}\\d{3}/\\d).*");
	
	public static void main(String[] args) throws FileNotFoundException, IOException {

		File input = new File(args[0]);
		File output = new File("src/main/resources/grouper-unclassified.cfg");
		BufferedReader br = new BufferedReader(new FileReader(input));
		BufferedWriter bw = new BufferedWriter(new FileWriter(output));
		
		// READING FIRST LINE
		String line = br.readLine();
		
		while (line != null) {
			Matcher matcher;
			
			// IF WE HAVE A LIST, LIST THE ENTRIES
			if ((matcher = listeEntree.matcher(line)).matches()) {
				bw.write("01:" + matcher.group(1) + "\n");
				// ACCEPT ONLY 2 LINES WITHOUT ENTRY DIAGS BEFORE BREAKING, OR A NEW LIST (CF PATTERNS)
				Integer nonMatched = 0;
				while((line = br.readLine()) != null) {
					// CHECK IF WE HAVE A CIM OR CCAM
					if ((matcher = cim.matcher(line)).matches() || (matcher = ccam.matcher(line)).matches()) {
						bw.write("02:" + matcher.group(1) + "\n");
						nonMatched = 0;
					}
					// NO CIM ELEMENT, CHECK IF IT IS A DIAGENTREE OR LISTEENTREE
					else {
						if (diagEntree.matcher(line).matches())
							break;
						if (listeEntree.matcher(line).matches())
							break;
						// NO DIAGENTREE OR LISTENTREE, IF IT IS THE THIRD LINE, GO AWAY
						if (nonMatched == 2)
							break;
						else
							nonMatched++;
					}
				}

				// DO NOT CONTINUE THIS LOOP, RESTART AT BEGINNING
				continue;
			}
			
			// NO MATCH, READ NEXT LINE
			line = br.readLine();
		}

		bw.close();
		br.close();

	}
}
