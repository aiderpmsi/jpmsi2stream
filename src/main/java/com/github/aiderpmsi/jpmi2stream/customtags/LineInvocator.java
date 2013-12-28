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

import com.github.aiderpmsi.jpmi2stream.utils.MemoryBufferedReader;
import com.github.aiderpmsi.jpmsi2stream.linestypes.PmsiLineType;

public class LineInvocator extends Action {

	private static final long serialVersionUID = 6787149348479640408L;
	
	private String linename, varname;

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
				(PmsiLineType) scInstance.getRootContext().get(linename);
		
		// Writes the result in the local var
		try {
			scInstance.getContext(getParentTransitionTarget()).setLocal(varname, line.isFound(memoryBufferedReader));
		} catch (IOException e) {
			throw new ModelException(e);
		}
		
	}

	public String getLinename() {
		return linename;
	}

	public void setLinename(String linename) {
		this.linename = linename;
	}

	public String getVarname() {
		return varname;
	}

	public void setVarname(String varname) {
		this.varname = varname;
	}

}
