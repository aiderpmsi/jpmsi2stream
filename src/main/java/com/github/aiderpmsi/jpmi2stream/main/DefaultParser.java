package com.github.aiderpmsi.jpmi2stream.main;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.scxml.SCXMLExecutor;
import org.apache.commons.scxml.model.CustomAction;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.xml.sax.helpers.DefaultHandler;

import com.github.aiderpmsi.jpmi2stream.customtags.LineInvocator;
import com.github.aiderpmsi.jpmi2stream.customtags.LineWriter;
import com.github.aiderpmsi.jpmi2stream.customtags.NumLineWriter;
import com.github.aiderpmsi.jpmi2stream.customtags.Print;
import com.github.aiderpmsi.jpmi2stream.main.options.DefaultParserOptions;
import com.github.aiderpmsi.jpmi2stream.utils.ClasspathHandler;
import com.github.aiderpmsi.jpmi2stream.utils.ExecutorFactory;
import com.github.aiderpmsi.jpmi2stream.utils.MemoryBufferedReader;

public class DefaultParser {

	public static void main(String[] args) {
		// Lecture des arguments :
		DefaultParserOptions options = new DefaultParserOptions();
        CmdLineParser parser = new CmdLineParser(options);
        
        // Lecture des arguments
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            if(options.isHelp()){
                parser.printUsage(System.out);
                return;
            } else if (options.isVersion()){
                System.out.println("Version : 1.9.0");
                return;
            } else {
            	parser.setUsageWidth(80);
            	parser.printUsage(System.out);
            	System.out.println(e.getMessage());
            	return;
            }
        }
        
		List<CustomAction> customActions = new ArrayList<CustomAction>();
		customActions.add(new CustomAction(
				"http://my.custom-actions.domain/CUSTOM", "lineinvocator", LineInvocator.class));
		customActions.add(new CustomAction(
				"http://my.custom-actions.domain/CUSTOM", "linewriter", LineWriter.class));
		customActions.add(new CustomAction(
				"http://my.custom-actions.domain/CUSTOM", "print", Print.class));
		customActions.add(new CustomAction(
				"http://my.custom-actions.domain/CUSTOM", "numlinewriter", NumLineWriter.class));

		// (2) Parse the SCXML document containing the custom action(s)
		try {
			URL scxmlLocation = new URL(null, "classpath:test.xml",
					new ClasspathHandler(ClassLoader.getSystemClassLoader()));
			//URL filelocation = new URL("file:///home/AIDER-delabre/Documents/test_rss");
			URL filelocation = options.getPmsiUrl();
			File exportlocation = options.getExportFile();
					
			ExecutorFactory machineFactory = new ExecutorFactory()
					.setScxmlDocument(scxmlLocation)
					.setCustomActions(customActions)
					.setErrorHandler(new DefaultHandler())
					.setMemoryBufferedReader(new MemoryBufferedReader(new InputStreamReader(filelocation.openStream())))
					.setOutputdocument(exportlocation);
			
			SCXMLExecutor machine = machineFactory.createMachine();
			machine.go();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
