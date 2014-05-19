package com.github.aiderpmsi.pims.resourcegen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

import com.github.aiderpmsi.pims.grouper.model.SimpleDictionary;

/**
 * Generated from grouper-acteclassant.cfg and grouper-classeacte.cfg
 * 
 * @author jpc
 *
 */
public class ActeClassantOpGen {

	public static void main(String[] args) throws FileNotFoundException, IOException {
			
		File input = new File("src/main/resources/grouper-acteclassant.cfg");
		SimpleDictionary dico = new SimpleDictionary("groupe-classeacte.xml");
		HashSet<String> acteschir = dico.getDefintion("ADC");
		File output = new File("src/main/resources/grouper-acteclassantop.cfg");
		BufferedReader br = new BufferedReader(new FileReader(input));
		BufferedWriter bw = new BufferedWriter(new FileWriter(output));
		bw.write("01:all\n");
		
		String line;
		while ((line = br.readLine()) != null) {
			if (line.startsWith("02:")) {
				String element = line.substring(3, line.length());
				if (acteschir.contains(element))
					bw.write("02:" + element + "\n");
			}
		}
		
		br.close();
		bw.close();
	}


}
