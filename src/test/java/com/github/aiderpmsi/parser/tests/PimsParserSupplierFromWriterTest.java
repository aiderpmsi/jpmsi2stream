package com.github.aiderpmsi.parser.tests;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.aiderpmsi.pims.parser.utils.PimsParserFromWriter;

public class PimsParserSupplierFromWriterTest {

    @Before
    public void setUp() {
   }
 
    @After
    public void tearDown() {
    	// DO NOTHING
    }
    
    @Test
    public void test1() throws Exception {
    	final StringBuilder sb = new StringBuilder();
    	final PimsParserFromWriter writer = new PimsParserFromWriter();
    	final List<Exception> error = new LinkedList<>();
    	
    	// READER THREAD
    	Runnable run = new Runnable() {
    		@Override
    		public void run() {
    			try {
    				while (writer.readLine() != null) {
    					sb.append(writer.readLine());
    					sb.append('|');
    					writer.consume(writer.readLine().length());
    				}
    			} catch (IOException e) {
    				error.add(e);
    			}
    		}
    	};
    	Thread thread = new Thread(run);
    	thread.start();
    	
    	// WRITE INTO WRITER
    	writer.write("Hello\n");
    	writer.write("Nb\n");
    	writer.close();
    	
    	thread.join();
    	
    	Assert.assertEquals(0, error.size());
    	Assert.assertEquals("Hello|Nb|", sb.toString());
    }

    @Test
    public void test2() throws Exception {
    	final StringBuilder sb = new StringBuilder();
    	final PimsParserFromWriter writer = new PimsParserFromWriter();
    	final List<Exception> error = new LinkedList<>();
    	
    	// READER THREAD
    	Runnable run = new Runnable() {
    		@Override
    		public void run() {
    			try {
    				while (writer.readLine() != null) {
    					sb.append(writer.readLine());
    					sb.append('|');
    					writer.consume(writer.readLine().length());
    				}
    			} catch (IOException e) {
    				error.add(e);
    			}
    		}
    	};
    	Thread thread = new Thread(run);
    	thread.start();
    	
    	// WRITE INTO WRITER
    	writer.write("Nb");
    	writer.close();
    	
    	thread.join();
    	
    	Assert.assertEquals(0, error.size());
    	Assert.assertEquals("Nb|", sb.toString());
    }
}