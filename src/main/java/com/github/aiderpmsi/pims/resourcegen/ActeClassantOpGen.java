package com.github.aiderpmsi.pims.resourcegen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;

import com.github.aiderpmsi.pims.grouper.model.SimpleDictionary;
import com.github.aiderpmsi.pims.grouper.model.SimpleDictionary.Type;

/**
 * Generated from grouper-acteclassant.cfg and grouper-classeacte.cfg
 * 
 * @author jpc
 *
 */
public class ActeClassantOpGen {

	public static void main(String[] args) throws FileNotFoundException, IOException {

		Path acteClassantPath = Paths.get("src/main/resources/com/github/aiderpmsi/pims/grouper/grouper-acteclassant.cfg");
		Path acteClassantOpPath = Paths.get("src/main/resources/com/github/aiderpmsi/pims/grouper/grouper-acteclassantop.cfg");
		
		// DICO CONTAINING CHIR ACTS
		SimpleDictionary dico = new SimpleDictionary(Type.classeActe);
		HashSet<String> acteschir = dico.getDefinition("ADC");

		try (BufferedWriter acteClassantOpWriter = Files.newBufferedWriter(acteClassantOpPath, Charset.forName("UTF-8"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

			// ALL ACTES CLASSANTS
			try (BufferedReader acteClassantReader = Files.newBufferedReader(acteClassantPath, Charset.forName("UTF-8"))) {

				acteClassantOpWriter.write("01:all\n");

				String readedLine;
				while ((readedLine = acteClassantReader.readLine()) != null) {
					if (readedLine.startsWith("02:") &&
							acteschir.contains(readedLine.substring(3, readedLine.length()))) {
						acteClassantOpWriter.write(readedLine);
						acteClassantOpWriter.write('\n');
					}
				
				}
			}

			// ACTES CLASSANTS BY NAME
			try (BufferedReader acteClassantReader = Files.newBufferedReader(acteClassantPath, Charset.forName("UTF-8"))) {
				
				String readedLine;
				while ((readedLine = acteClassantReader.readLine()) != null) {
					if (readedLine.startsWith("01:")) {
						acteClassantOpWriter.write(readedLine);
						acteClassantOpWriter.write('\n');
					} else if (readedLine.startsWith("02:") &&
							acteschir.contains(readedLine.substring(3, readedLine.length()))) {
						acteClassantOpWriter.write(readedLine);
						acteClassantOpWriter.write('\n');
					}
				}
				
			}
		}
		
	}


}
