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
 * We find the list of excluded DP in sts_20130005_0001_p000.pdf pages 268 to 381
 * (pdftotext sts_20130005_0001_p000.pdf -nopgbrk -f 268 -l 381)
 * 
 * @author jpc
 *
 */
public class Cmagen {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		Pattern cimrec = Pattern.compile("^[A-Z]\\d+\\.\\d+(?:\\+\\d+)?");
		
		File input = new File(args[0]);
		File output = new File("src/main/resources/cma.xml");
		BufferedReader br = new BufferedReader(new FileReader(input));
		BufferedWriter bw = new BufferedWriter(new FileWriter(output));
		
		// START THE XML
		
		bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		bw.write("<cmas>\n");
		
		String line, cim, gravity, excdp, exccm;

		while ((line = br.readLine()) != null) {
			Matcher matcher = cimrec.matcher(line);
			// WE HAVE ONE CIM DIAGNOSIS
			if (matcher.matches()) {
				cim = matcher.group();
				// NEXT LINE IS VOID
				br.readLine();
				// NEXT LINE IS THE LEVEL OF GRAVITY
				gravity = br.readLine().trim();
				// NEXT LINE IS VOID
				br.readLine();
				// NEXT LINE IS EXCLUDED DP LIST NUMBER
				excdp = br.readLine().trim();
				// NEXT LINE IS VOID
				br.readLine();
				// NEXT LINE IS EXCLUDED CM LIST NUMBER
				exccm = br.readLine().trim();
				bw.write("    <cma cim=\"" + cim + "\" >\n");
				bw.write("        <gravity>" + gravity + "</gravity>\n");
				bw.write("        <exclude>CMA-DP-" + excdp + "</exclude>\n");
				if (!exccm.equals("-"))
					bw.write("        <exclude>CMA-CM-" + exccm + "</exclude>\n");
				bw.write("    </cma>\n");
			}
		}
		
		// FINISHES THE CMAS
		bw.write("<cmas>\n");
		
		br.close();
		bw.close();
	}

}
