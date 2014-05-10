package com.github.aiderpmsi.pims.resourcegen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;
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
		File output = new File("src/main/resources/grouper-acteclassant.cfg");
		BufferedReader br = new BufferedReader(new FileReader(input));
		BufferedWriter bw = new BufferedWriter(new FileWriter(output));

		// ONE GMH // MULTIPLE CCAM
		TreeMap<String, TreeSet<String>> cmds = new TreeMap<>();
		String line;
		
		while ((line = br.readLine()) != null) {
			Matcher matcher = ccam.matcher(line);
			if (matcher.matches()) {
				// WE HAVE ONE CCAM WITH ASSOCIATED MULTIPLE CMD
				// GET THE CMD HASHSET FOR EACH CMD OR CREATE IT IF NEEDED
				String[] cmdsRead = matcher.group(2).split("\\s+");
				for (String cmd : cmdsRead) {
					TreeSet<String> ccamSet = cmds.get(cmd);
					if (ccamSet == null) {
						ccamSet = new TreeSet<>();
						cmds.put(cmd, ccamSet);
					}
					// ENTER THIS CCAM FOR THIS CMD
					ccamSet.add(matcher.group(1));
				}
			}
		}

		// NOW WE WRITE THE LIST OF CCCAM FOR EACH CMD
		for (Entry<String, TreeSet<String>> entrycmd : cmds.entrySet()) {
			bw.write("0:" + entrycmd.getKey() + "\n");
			for (String ccamRead : entrycmd.getValue()) {
				bw.write("1:" + ccamRead + "\n");
			}
		}

		br.close();
		bw.close();
	}


}
