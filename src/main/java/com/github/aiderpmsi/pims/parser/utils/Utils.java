package com.github.aiderpmsi.pims.parser.utils;

import java.io.IOException;

import com.github.aiderpmsi.pims.parser.linestypes.ConfiguredPmsiLine;
import com.github.aiderpmsi.pims.parser.linestypes.EndOfFilePmsiLine;
import com.github.aiderpmsi.pims.parser.linestypes.IPmsiLine;
import com.github.aiderpmsi.pims.parser.linestypes.IPmsiLine.Element;
import com.github.aiderpmsi.pims.parser.linestypes.LineNumberPmsiLine;
import com.github.aiderpmsi.pims.parser.linestypes.Segment;

public class Utils {
	
	@FunctionalInterface
	public interface LineHandler {
		public void handle(final IPmsiLine pmsiLine) throws IOException;
	}
	
	@FunctionalInterface
	public interface ErrorHandler {
		public void error(final String msg, final long line) throws IOException;
	}

	private final LineHandler lineHandler;
	
	private final MemoryBufferedReader mbr;

	private final ErrorHandler erh;
	
	public Utils(final MemoryBufferedReader mbr, final LineHandler lineWriter, final ErrorHandler erh) {
		this.lineHandler = lineWriter;
		this.mbr = mbr;
		this.erh = erh;
	}

	public void noLineError(final String error, final long numLine) throws IOException {
		erh.error(error + " were attended but not found", numLine);
	}
	
	public void noHeaderError(final long numLine) throws IOException {
		erh.error("No header found", numLine);
	}
	
	public void handleLineNumber(final long lineNumber, final LineNumberPmsiLine lineNumberPmsiLine) throws IOException {
		lineNumberPmsiLine.setLineNumber(lineNumber);
		lineHandler.handle(lineNumberPmsiLine);
	}
	
	public boolean isFound(final IPmsiLine pmsiLine) throws IOException {
		if (pmsiLine instanceof EndOfFilePmsiLine) {
			if (mbr.readLine() == null)
				return true;
			else
				return false;
		} else if (pmsiLine instanceof LineNumberPmsiLine) {
			return false;
		} else if (pmsiLine instanceof ConfiguredPmsiLine) {
			final ConfiguredPmsiLine cPmsiLine = (ConfiguredPmsiLine) pmsiLine;
			if (mbr.readLine() == null || mbr.readLine().length() < cPmsiLine.getLineSize())
				return false;
			else {
				final Segment sgt = new Segment(mbr.readLine().toCharArray(), 0, cPmsiLine.getLineSize());
				return cPmsiLine.matches(sgt);
			}
		} else {
			throw new IOException("Line type " + pmsiLine.getClass() + " unknown");
		}
	}

	public void handleLine(final IPmsiLine pmsiLine) throws IOException {
		lineHandler.handle(pmsiLine);
		mbr.consume(pmsiLine.getLineSize());
	}
	
	public Integer getElementLineAsInt(final IPmsiLine pmsiLine, String elementName) {
		for (final Element element : pmsiLine.getElements()) {
			if (element.getName().equals(elementName)) {
				return Integer.parseInt(element.getElement().toString());
			}
		}
		return null;
	}
}
