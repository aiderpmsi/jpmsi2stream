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
 * We find the list of CMD for each CIM volume_1.pdf pages 430 to 513
 * (pdftotext volume_1.pdf -nopgbrk -raw -f 430 -l 513)
 * 
 * @author jpc
 *
 */
public class DPClassantGen {

	private static Pattern cim_cmd = Pattern.compile("^([A-Z]\\d+(?:\\.\\d+(?:\\+\\d+)?)?)\\s+(\\d+).*");

	public static void main(String[] args) throws FileNotFoundException, IOException {
			
		File input = new File(args[0]);
		File output = new File("src/main/resources/grouper-dpclassant.cfg");
		BufferedReader br = new BufferedReader(new FileReader(input));
		BufferedWriter bw = new BufferedWriter(new FileWriter(output));
		
		String line;
		
		// HASHMAP CMD // HASHSET OF DP
		TreeMap<String, TreeSet<String>> cmd_cim = new TreeMap<>();

		while ((line = br.readLine()) != null) {
			Matcher matcher = cim_cmd.matcher(line);
			if (matcher.matches()) {
				// WE HAVE ONE CIM WITH ASSOCIATED CMD
				// GET ITS HASHSET OR CREATE IT IF NEEDED
				TreeSet<String> cim = cmd_cim.get(matcher.group(2));
				if (cim == null) {
					cim = new TreeSet<>();
					cmd_cim.put(matcher.group(2), cim);
				}
				// ENTERS THIS CIM IN CMD SET
				cim.add(matcher.group(1));
			}
		}
		
		// NOW WE WRITE THE LIST OF CIM FOR EACH CMD
		for (Entry<String, TreeSet<String>> entrycmd : cmd_cim.entrySet()) {
			bw.write("01:CMD-" + entrycmd.getKey() + "\n");
			for (String cim : entrycmd.getValue()) {
				bw.write("02:" + cim + "\n");
			}
		}

		br.close();
		bw.close();
	}


}
