package com.vaynberg.wicket.select2;

import junit.framework.Assert;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

/**
 * A bogus test to keep bamboo happy since its looking for test output
 * 
 * @author igor
 * 
 */
public class SelectTest {
    
    private WicketTester tester;
    
    @Before
    public void bogus() {	
	tester = new WicketTester();
    }
    
    @Test
    public void testMultiSelect() {
	TestMultiSelectPage testPage =tester.startPage(TestMultiSelectPage.class);	
	tester.assertRenderedPage(TestMultiSelectPage.class);
	tester.getRequest().setParameter("countries", Country.CU.name()+","+Country.CA.name());	
	tester.submitForm(testPage.getForm());
	Assert.assertTrue(testPage.getCountries().contains(Country.CU));
	Assert.assertTrue(testPage.getCountries().contains(Country.CA));
	Assert.assertTrue(!testPage.getCountries().contains(Country.US));
	
    }
}
