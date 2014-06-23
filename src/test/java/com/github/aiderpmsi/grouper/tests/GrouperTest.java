package com.github.aiderpmsi.grouper.tests;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.aiderpmsi.pims.grouper.model.RssContent;
import com.github.aiderpmsi.pims.grouper.tags.GroupFactory.Group;
import com.github.aiderpmsi.pims.grouper.utils.Grouper;
import com.github.aiderpmsi.pims.treebrowser.TreeBrowserException;
public class GrouperTest {
 
	private Grouper gp;
	
    @Before
    public void setUp() throws TreeBrowserException {
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

    @Test
    public void test28Z04Zb() throws Exception {
		List<RssContent> cont = new T28Z04Zb();

		Group group = gp.group(cont);

		test(group, "28Z04", "Z", "", "");
    }
    
    @Test
    public void test11K021J() throws Exception {
		List<RssContent> cont = new T11K021J();

		Group group = gp.group(cont);

		test(group, "11K02", "1", "J", "");
    }
    
    @Test
    public void testMultigrouper() throws Exception {
		List<RssContent> cont1 = new T28Z04Zb();
		List<RssContent> cont2 = new T15Z10E();

		for (int i = 0 ; i < 1000 ; i++) {
			gp.group(cont1);
			gp.group(cont2);
		}

    }
    
    
    private void test(Group gp, String racine, String gravite, String modalite, String erreur) {
		Assert.assertNotNull(gp);
		Assert.assertEquals(racine, gp.racine);
		Assert.assertEquals(gravite, gp.gravite);
		Assert.assertEquals(modalite, gp.modalite);
		Assert.assertEquals(erreur, gp.erreur);
    }
}