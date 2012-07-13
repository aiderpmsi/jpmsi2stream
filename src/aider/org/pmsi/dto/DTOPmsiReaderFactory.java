package aider.org.pmsi.dto;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;

import net.xqj.sedna.SednaXQDataSource;

import aider.org.pmsi.parser.PmsiReader;
import aider.org.pmsi.reader.PmsiRSF2009Reader;
import aider.org.pmsi.reader.PmsiRSF2012Reader;
import aider.org.pmsi.reader.PmsiRSS116Reader;

public class DTOPmsiReaderFactory {

	private XQConnection connection = null;
	
	public DTOPmsiReaderFactory() throws XQException  {
		XQDataSource xqs = new SednaXQDataSource();
	    xqs.setProperty("serverName", "localhost");
	    xqs.setProperty("databaseName", "testdb");
	    
	    XQConnection conn = xqs.getConnection("SYSTEM", "PASSWORD");
	    conn.setAutoCommit(false);
	    
	    connection =conn;
	}

	public DTOPmsiLineType getDtoPmsiLineType(PmsiReader<?, ?> reader) throws UnsupportedEncodingException, IOException, XQException, InterruptedException {
		if (reader instanceof PmsiRSF2009Reader) {
			return new DtoRsf2009(connection);
		} else if (reader instanceof PmsiRSF2012Reader) {
			return new DtoRsf2012(connection);
		} else if (reader instanceof PmsiRSS116Reader) {
			return new DtoRss116(connection);
		}
		return null;
	}

	public void close() throws XQException  {
		if (connection != null) {
			connection.close();
			connection = null;
		}
	}
}
