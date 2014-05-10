package com.github.aiderpmsi.grouper.tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.aiderpmsi.pims.grouper.customtags.Group;
import com.github.aiderpmsi.pims.grouper.model.RssContent;
import com.github.aiderpmsi.pims.grouper.utils.Grouper;
public class GrouperTest {
 
    @Before
    public void setUp() {
    	// DO NOTHING
    }
 
    @After
    public void tearDown() {
    	// DO NOTHING
    }
 
    @Test
    public void test15M04E() throws Exception {
		Grouper gp = new Grouper(); 
		RssContent cont = new T15M04E();

		Group group = gp.group(cont);

		test(group, "15M04", "E", "", "");
    }
 
    @Test
    public void test15Z10E() throws Exception {
		Grouper gp = new Grouper(); 
		RssContent cont = new T15Z10E();

		Group group = gp.group(cont);

		test(group, "15Z10", "E", "", "");
    }

    private void test(Group gp, String racine, String gravite, String modalite, String erreur) {
		Assert.assertNotNull(gp);
		Assert.assertEquals(racine, gp.getRacine());
		Assert.assertEquals(gravite, gp.getGravite());
		Assert.assertEquals(modalite, gp.getModalite());
		Assert.assertEquals(erreur, gp.getErreur());
    }
}