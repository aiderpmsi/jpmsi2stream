package com.github.aiderpmsi.grouper.tests;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.aiderpmsi.pims.grouper.model.RssContent;
import com.github.aiderpmsi.pims.grouper.tags.Group;
import com.github.aiderpmsi.pims.grouper.utils.Grouper;
public class GrouperTest {
 
	private Grouper gp;
	
    @Before
    public void setUp() {
    	gp = new Grouper();
   }
 
    @After
    public void tearDown() {
    	// DO NOTHING
    }
 
    @Test
    public void test28Z04Z() throws Exception {
		List<RssContent> cont = new T28Z04Z();

		Group group = gp.group(cont);

		test(group, "28Z04", "Z", "", "");
    }

    @Test
    public void test15M04E() throws Exception {
		List<RssContent> cont = new T15M04E();

		Group group = gp.group(cont);

		test(group, "15M04", "E", "", "");
    }
 
    @Test
    public void test15Z10E() throws Exception {
		List<RssContent> cont = new T15Z10E();

		Group group = gp.group(cont);

		test(group, "15Z10", "E", "", "");
    }

    @Test
    public void test01C041() throws Exception {
		List<RssContent> cont = new T01C041();

		Group group = gp.group(cont);

		test(group, "01C04", "1", "", "");
    }

    @Test
    public void test01K06J() throws Exception {
		List<RssContent> cont = new T01K06J();

		Group group = gp.group(cont);

		test(group, "01K06", "", "J", "");
    }

    @Test
    public void test08M231() throws Exception {
		List<RssContent> cont = new T08M231();

		Group group = gp.group(cont);

		test(group, "08M23", "1", "", "");
    }

    private void test(Group gp, String racine, String gravite, String modalite, String erreur) {
		Assert.assertNotNull(gp);
		Assert.assertEquals(racine, gp.getRacine());
		Assert.assertEquals(gravite, gp.getGravite());
		Assert.assertEquals(modalite, gp.getModalite());
		Assert.assertEquals(erreur, gp.getErreur());
    }
}