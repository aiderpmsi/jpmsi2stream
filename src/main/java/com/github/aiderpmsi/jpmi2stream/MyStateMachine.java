package com.github.aiderpmsi.jpmi2stream;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.apache.commons.scxml.SCXMLExecutor;
import org.apache.commons.scxml.SCXMLListener;
import org.apache.commons.scxml.TriggerEvent;
import org.apache.commons.scxml.model.ModelException;
import org.apache.commons.scxml.model.Transition;
import org.apache.commons.scxml.model.TransitionTarget;
import org.xml.sax.ContentHandler;

import aider.org.pmsi.parser.linestypes.PmsiLineType;

public class MyStateMachine {
	
    /**
     * The method signature for the activities corresponding to each
     * state in the SCXML document.
     */
    private static final Class<?>[] SIGNATURE = new Class[0];

    /**
     * The method parameters for the activities corresponding to each
     * state in the SCXML document.
     */
    private static final Object[] PARAMETERS = new Object[0];
	
	private SCXMLExecutor engine;
	
	private HashMap<String, PmsiLineType> lignes;
	
	private ContentHandler contentHandler;

	private MemoryBufferedReader memoryBufferedReader;
	
	public MyStateMachine(SCXMLExecutor engine, HashMap<String, PmsiLineType> lignes,
			ContentHandler contentHandler, MemoryBufferedReader memoryBufferedReader) {
		this.engine = engine;
		this.lignes = lignes;
		this.contentHandler = contentHandler;
		this.memoryBufferedReader = memoryBufferedReader;
	}
	
	public void test_header() throws IOException, ModelException {
		System.out.println("test_header");
		if (lignes.get("rsf2012Header").isFound(memoryBufferedReader)) {
			engine.triggerEvent(new TriggerEvent("rsf2012h", TriggerEvent.SIGNAL_EVENT));
		} else {
			engine.triggerEvent(new TriggerEvent("none", TriggerEvent.SIGNAL_EVENT));
		}
	}

	public void headernotfound() {
		System.out.println("headernotfound");
	}

	public void rsf2012h() throws IOException {
		System.out.println("rsf2012h");
		lignes.get("rsf2012Header").writeResults(contentHandler);
	}
	
	protected class EntryListener implements SCXMLListener {

        public void onEntry(final TransitionTarget entered) {
            invoke(entered.getId());
        }

        public void onTransition(final TransitionTarget from,
                final TransitionTarget to, final Transition transition) {
            // nothing to do
        }

        public void onExit(final TransitionTarget exited) {
            // nothing to do
        }

    } 
	
	public void invoke(final String methodName) throws ModelException {
        Class<? extends MyStateMachine> clas = this.getClass();
        try {
            Method method = clas.getDeclaredMethod(methodName, SIGNATURE);
            method.invoke(this, PARAMETERS);
        } catch (Exception se) {
        	logError(se);
        }
    }
}
