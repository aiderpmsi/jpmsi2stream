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
 * We find the list of excluded DP in volume_1.pdf pages 260 to 353
 * (pdftotext volume_1.pdf -raw -nopgbrk -f 260 -l 353)
 * 
 * @author jpc
 *
 */
public class CmaGen {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		Pattern cimrec = Pattern.compile("^([A-Z]\\d+(?:.\\d+(?:\\++\\d+)?)?)\\s+(\\d+)\\s+(\\d+)\\s+((?:-)|(?:\\d+))\\s+.*");
		
		File input = new File(args[0]);
		File output = new File("src/main/resources/grouper-cma.cfg");
		BufferedReader br = new BufferedReader(new FileReader(input));
		BufferedWriter bw = new BufferedWriter(new FileWriter(output));
				
		String line;

		while ((line = br.readLine()) != null) {
			Matcher matcher = cimrec.matcher(line);
			// WE HAVE ONE CIM DIAGNOSIS
			if (matcher.matches()) {
				// 01: IS CIM NUMBER
				bw.write("01:" + matcher.group(1) + "\n");
				// 02: IS GRAVITY NUMBER
				bw.write("02:" + matcher.group(2) + "\n");
				// 03: THIS LIST OF DPS EXCLUDE THIS CMA FOR THIS CIM ELEMENT ASSOCIATED DIAGNOSIS
				bw.write("03:" + matcher.group(3) + "\n");
				// 04: THIS LIST OF CM EXCLUDE THIS CMA FOR THIS CIM ELEMENT ASSOCIATED DIAGNOSIS
				if (!matcher.group(4).equals("-"))
					bw.write("04:" + matcher.group(4) + "\n");
			}
		}

		br.close();
		bw.close();
	}

}
