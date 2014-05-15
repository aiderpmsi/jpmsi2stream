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
 * We find the list of excluded DP in volume_1.pdf pages 356 to 409
 * (pdftotext volume_1.pdf -nopgbrk -raw -f 356 -l 409)
 * 
 * @author jpc
 *
 */
public class CmaExcludeDPGen {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		Pattern dpexcpat = Pattern.compile("^(\\d+)\\s(.+)");
		Pattern excpat = Pattern.compile("^[A-Z]\\d{2}.*");
		
		File input = new File(args[0]);
		File output = new File("src/main/resources/grouper-cma-exc-dp.cfg");
		BufferedReader br = new BufferedReader(new FileReader(input));
		BufferedWriter bw = new BufferedWriter(new FileWriter(output));
		
		String line, dpexcnumber;

		while ((line = br.readLine()) != null) {
			Matcher matcher;
			// WE HAVE ONE LIST OF DP EXCLUDING THIS CMA
			if ((matcher  = dpexcpat.matcher(line)).matches()) {
				dpexcnumber = matcher.group(1);
				bw.write("01:" + dpexcnumber + "\n");
				// SPLIT THE DPS
				split(matcher.group(2), bw);
			} else if ((matcher  = excpat.matcher(line)).matches()) {
				split(matcher.group(), bw);
			}
		}
		
		br.close();
		bw.close();
	}

	public static void split(String line, BufferedWriter bw) throws IOException {
		for (String dp : line.split("\\s+")) {
			// FOR EACH EXCLUDED DP, CHECK IF IT IS A GROUP OR JUST AN ELEMENT
			if (dp.contains("-")) {
				// IF WE HAVE A START AND END DP, STORE AS A GROUP
				for (String group : dp.split("-"))
					bw.write("03:" + group + "\n");
			} else {
				// STORE AS SINGLE ELEMENT
				bw.write("02:" + dp + "\n");
			}
		}
	}
}
