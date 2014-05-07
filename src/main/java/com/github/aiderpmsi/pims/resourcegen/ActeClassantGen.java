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
 * We find the list of CMD for each CCMA sts_20130005_0001_p000.pdf pages 539 to 599
 * (pdftotext sts_20130005_0001_p000.pdf -nopgbrk -raw -f 539 -l 599)
 * 
 * @author jpc
 *
 */
public class ActeClassantGen {

	public static void main(String[] args) throws FileNotFoundException, IOException {
	
		Pattern ccam = Pattern.compile("^([A-Z]{4}\\d{3}/\\d) (.*)");
		
		File input = new File(args[0]);
		File output = new File("src/main/resources/grouper-acteclassant.xml");
		BufferedReader br = new BufferedReader(new FileReader(input));
		BufferedWriter bw = new BufferedWriter(new FileWriter(output));
		
		// START THE XML
		
		bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		bw.write("<actes>\n");
		
		String line;

		while ((line = br.readLine()) != null) {
			Matcher matcher = ccam.matcher(line);
			// WE HAVE ONE LIST OF GHM FOR THIS CCAM
			if (matcher.matches()) {
				bw.write("    <acte id=\"" + matcher.group(1) + "\" >\n");
				// SPLIT THE LIST OF CMD
				String[] chunks = matcher.group(2).split(" ");
				for (String chunk : chunks) {
					if (chunk.length() != 0) {
						bw.write("        <CMD>" + chunk + "</CMD>\n");
					}
				}
				bw.write("    </acte>\n");
			}
		}
		
		// FINISHES THE ACTES
		bw.write("<actes>\n");
		
		br.close();
		bw.close();
	}


}
