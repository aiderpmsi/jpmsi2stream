package com.github.aiderpmsi.jpmi2stream;

import java.io.IOException;
import org.apache.commons.scxml.Context;
import org.apache.commons.scxml.Evaluator;
import org.apache.commons.scxml.TriggerEvent;
import org.apache.commons.scxml.env.AbstractStateMachine;
import org.apache.commons.scxml.model.ModelException;
import org.apache.commons.scxml.model.SCXML;
import org.xml.sax.ContentHandler;

import aider.org.pmsi.parser.linestypes.PmsiRsf2012Header;

public class MyStateMachine extends AbstractStateMachine {
	
	protected MemoryBufferedReader memoryBufferedReader = null;
	
	protected PmsiRsf2012Header pmsiRsf2012Header = null;
	
	protected ContentHandler contentHandler = null;

	public MyStateMachine(SCXML stateMachine, Context rootCtx,
			Evaluator evaluator) {
		this.memoryBufferedReader = (MemoryBufferedReader) rootCtx.get("_memoryBufferedReader");
		this.pmsiRsf2012Header = (PmsiRsf2012Header) rootCtx.get("_rsf2012Header");
		this.contentHandler = (ContentHandler) rootCtx.get("_contentHandler");
	}

	public void test_header() throws IOException, ModelException {
		System.out.println("test_header");
		if (pmsiRsf2012Header.isFound(memoryBufferedReader)) {
			getEngine().triggerEvent(new TriggerEvent("rsf2012h", TriggerEvent.SIGNAL_EVENT));
		} else {
			getEngine().triggerEvent(new TriggerEvent("none", TriggerEvent.SIGNAL_EVENT));
		}
	}

	public void headernotfound() {
		System.out.println("headernotfound");
	}

	public void rsf2012h() throws IOException {
		System.out.println("rsf2012h");
		pmsiRsf2012Header.writeResults(contentHandler);
	}
	
}
