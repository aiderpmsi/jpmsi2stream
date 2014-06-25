package com.github.aiderpmsi.pims.parser.utils;

import java.io.IOException;

import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.github.aiderpmsi.pims.parser.linestypes.EndOfFilePmsiLine;
import com.github.aiderpmsi.pims.parser.linestypes.IPmsiLine;
import com.github.aiderpmsi.pims.parser.linestypes.ConfiguredPmsiLine;
import com.github.aiderpmsi.pims.parser.linestypes.LineNumberPmsiLine;
import com.github.aiderpmsi.pims.parser.linestypes.Segment;

public class Utils {
	
	@FunctionalInterface
	public interface LineWriter {
		public void write(IPmsiLine pmsiLine, ContentHandler ch) throws IOException;
	}

	private LineWriter lineWriter;
	
	private MemoryBufferedReader mbr;

	private ContentHandler ch;

	private ErrorHandler erh;
	
	public Utils(MemoryBufferedReader mbr, LineWriter lineWriter, ContentHandler ch, ErrorHandler erh) {
		this.lineWriter = lineWriter;
		this.mbr = mbr;
		this.ch = ch;
		this.erh = erh;
	}

	public void noLineError(String error, long numLine) throws SAXException {
		erh.error(new SAXParseException(error + " were attended but not found", "pimsparser", "pimsparser", (int) numLine, 0));
	}
	
	public void noHeaderError(long numLine)  throws SAXException {
		erh.error(new SAXParseException("No header found", "pimsparser", "pimsparser", (int) numLine, 0));
	}
	
	public void writelinenumber(long lineNumber, LineNumberPmsiLine lineNumberPmsiLine) throws IOException {
		lineNumberPmsiLine.setLineNumber(lineNumber);
		lineWriter.write(lineNumberPmsiLine, ch);
	}
	
	public boolean isFound(IPmsiLine pmsiLine) throws IOException {
		if (pmsiLine instanceof EndOfFilePmsiLine) {
			if (mbr.readLine() == null)
				return true;
			else
				return false;
		} else if (pmsiLine instanceof LineNumberPmsiLine) {
			return false;
		} else if (pmsiLine instanceof ConfiguredPmsiLine) {
			ConfiguredPmsiLine cPmsiLine = (ConfiguredPmsiLine) pmsiLine;
			if (mbr.readLine().length() < cPmsiLine.getLineSize())
				return false;
			else {
				Segment sgt = new Segment(mbr.readLine().toCharArray(), 0, cPmsiLine.getLineSize());
				return cPmsiLine.matches(sgt);
			}
		} else {
			throw new IOException("Line type " + pmsiLine.getClass() + " unknown");
		}
	}

	public void writeElement(IPmsiLine pmsiLine) throws IOException {
		lineWriter.write(pmsiLine, ch);
		mbr.consume(pmsiLine.getLineSize());
	}
	
}
