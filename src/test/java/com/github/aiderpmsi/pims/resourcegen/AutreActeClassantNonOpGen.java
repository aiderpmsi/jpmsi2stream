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
 * We find the list of CMD for each CCMA volume_1.pdf pages 608 to 612
 * (pdftotext volume_1.pdf -nopgbrk -raw -f 636 -l 647)
 * 
 * @author jpc
 *
 */
public class AutreActeClassantNonOpGen {

	public static void main(String[] args) throws FileNotFoundException, IOException {
	
		Pattern ccam = Pattern.compile("^([A-Z]{4}\\d{3}/\\d) (.*)");
		
		File input = new File(args[0]);
		File output = new File("src/main/resources/grouper-autreacteclassantnonop.cfg");
		BufferedReader br = new BufferedReader(new FileReader(input));
		BufferedWriter bw = new BufferedWriter(new FileWriter(output));

		bw.write("01:all\n");
		String line;
		
		while ((line = br.readLine()) != null) {
			Matcher matcher = ccam.matcher(line);
			if (matcher.matches()) {
				// WE HAVE ONE CCAM 
				bw.write("02:" + matcher.group(1) + "\n");
			}
		}

		br.close();
		bw.close();
	}


}
