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
public class Groupergen {

	private static Pattern diagEntree = Pattern.compile("^Diagnostics d'entrée dans la CMD n° (\\d+)");
	private static Pattern listeEntree = Pattern.compile("^Liste ([A-Z]-\\d+).*");
	private static Pattern cim = Pattern.compile("^([A-Z]\\d+(?:\\.\\d+(?:\\+\\d+)?)?).*");
	private static Pattern ccam = Pattern.compile("^([A-Z]{4}\\d{3}/\\d).*");
	
	public static void main(String[] args) throws FileNotFoundException, IOException {

		File input = new File(args[0]);
		File output = new File("src/main/resources/groupdefs.xml");
		BufferedReader br = new BufferedReader(new FileReader(input));
		BufferedWriter bw = new BufferedWriter(new FileWriter(output));
		
		// START THE XML
		
		bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		bw.write("<groups>\n");
		
		// READING FIRST LINE
		String line = br.readLine();
		
		while (line != null) {
			// IF WE HAVE A LIST OF ENTRY DIAGS, LIST THEM
			Matcher matcher = diagEntree.matcher(line);
			if (matcher.matches()) {
				bw.write("    <group name=\"CMD-" + matcher.group(1) + "\">\n");
				Integer nonMatched = 0;
				do {
					line = br.readLine();
					matcher = cim.matcher(line);
					// THIS LINE HAS ONE CIM ELEMENT
					if (matcher.matches()) {
						bw.write("        <element>" + matcher.group(1) + "</element>\n");
						nonMatched = 0;
					}
					// NO CIM ELEMENT, CHECK IF IT IS A DIAGENTREE OR LISTEENTREE
					else {
						matcher = diagEntree.matcher(line);
						if (matcher.matches())
							break;
						matcher = listeEntree.matcher(line);
						if (matcher.matches())
							break;
						// NO DIAGENTREE OR LISTENTREE, IF IT IS THE THIRD LINE, GO AWAY
						if (nonMatched == 2)
							break;
						else
							nonMatched++;
					}
				} while (line != null);
				bw.write("    </group>\n");
				continue;
			}

			// IF WE HAVE A LIST, LIST THE ENTRIES
			matcher = listeEntree.matcher(line);
			if (matcher.matches()) {
				bw.write("    <group name=\"" + matcher.group(1) + "\">\n");
				Integer nonMatched = 0;
				do {
					line = br.readLine();
					// CHECK IF WE HAVE A CIM OR CCAM
					if ((matcher = cim.matcher(line)).matches() || (matcher = ccam.matcher(line)).matches()) {
						bw.write("        <element>" + matcher.group(1) + "</element>\n");
						nonMatched = 0;
					}
					// NO CIM ELEMENT, CHECK IF IT IS A DIAGENTREE OR LISTEENTREE
					else {
						matcher = diagEntree.matcher(line);
						if (matcher.matches())
							break;
						matcher = listeEntree.matcher(line);
						if (matcher.matches())
							break;
						// NO DIAGENTREE OR LISTENTREE, IF IT IS THE THIRD LINE, GO AWAY
						if (nonMatched == 2)
							break;
						else
							nonMatched++;
					}
				} while (line != null);
				bw.write("    </group>\n");
				continue;
			}
			
			// NO MATCH, READ NEXT LINE
			line = br.readLine();
		}

		bw.write("</groups>\n");
		
		bw.close();
		br.close();

	}
}
