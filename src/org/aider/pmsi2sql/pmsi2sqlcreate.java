package org.aider.pmsi2sql;

import java.io.StringReader;
import java.sql.Connection;

import org.aider.pmsi2sql.linetypes.PmsiInsertion;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

public class pmsi2sqlcreate {

	public static void main(String[] args) throws Exception {
		
		Pmsi2SqlBaseOptions options = new Pmsi2SqlBaseOptions();

        CmdLineParser parser = new CmdLineParser(options);

        try {
            parser.parseArgument(args);

            	Connection myConn = options.getNewSqlConnection();
            	PmsiInsertion myInsertionTable = new PmsiInsertion("");
            	myConn.createStatement().execute(myInsertionTable.getSQLTable());
            	myConn.createStatement().execute(myInsertionTable.getSQLIndex());
            	myConn.createStatement().execute(myInsertionTable.getSQLFK());
            	myConn.commit();
            	myConn.close();
            	
        		PmsiRSSReader r = new PmsiRSSReader(new StringReader(""), options.getNewSqlConnection());

            	r.createTables();
            	r.createIndexes();
            	r.createKF();
            	r.commit();

        		PmsiRSFReader f = new PmsiRSFReader(new StringReader(""), options.getNewSqlConnection());
        		
        		f.createTables();
        		f.createIndexes();
        		f.createKF();
        		f.commit();
        		
                System.out.println("Done!");


        } catch (CmdLineException e) {
            if(options.isHelp()){
                parser.printUsage(System.out);
            } else if (options.isVersion()){
                System.out.println("Version : test");
            } else {
            parser.setUsageWidth(80);
            parser.printUsage(System.out);
            System.out.println(e.getMessage());
            }
        }	
        
	}
	
}
