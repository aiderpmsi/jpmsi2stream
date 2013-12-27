package com.github.aiderpmsi.jpmi2stream.customtags;

import java.io.IOException;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.scxml.ErrorReporter;
import org.apache.commons.scxml.EventDispatcher;
import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.SCXMLExpressionException;
import org.apache.commons.scxml.model.Action;
import org.apache.commons.scxml.model.ModelException;
import aider.org.pmsi.parser.linestypes.PmsiLineType;

import com.github.aiderpmsi.jpmi2stream.MemoryBufferedReader;

public class LineInvocator extends Action {

	private static final long serialVersionUID = 6787149348479640408L;
	
	private String lineName, varName;

	public LineInvocator() {
		super();
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public void execute(EventDispatcher evtDispatcher, ErrorReporter errRep,
			SCInstance scInstance, Log appLog, Collection derivedEvents)
			throws ModelException, SCXMLExpressionException {

		// Gets The file instance
		MemoryBufferedReader memoryBufferedReader =
				(MemoryBufferedReader) scInstance.getRootContext().get("_file");

		// Gets the line definition
		PmsiLineType line = 
				(PmsiLineType) scInstance.getRootContext().get(lineName);
		
		// Writes the result in the local var
		try {
			scInstance.getRootContext().setLocal(varName, line.isFound(memoryBufferedReader));
		} catch (IOException e) {
			throw new ModelException(e);
		}
		
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getVarName() {
		return varName;
	}

	public void setVarName(String varName) {
		this.varName = varName;
	}

}
