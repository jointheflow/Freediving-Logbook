package org.gianluca.logbook.test.dao;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.gianluca.logbook.dao.exception.FreediverIDException;
import org.gianluca.logbook.dao.googledatastore.LogbookDAO;
import org.gianluca.logbook.dao.googledatastore.entity.DiveSession;
import org.gianluca.logbook.dao.googledatastore.entity.DiveSessionsOfFreeediver;
import org.gianluca.logbook.dao.googledatastore.entity.Freediver;
import org.gianluca.logbook.helper.LogbookConstant;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class DiveSessionTest {

	private final LocalServiceTestHelper helper =  
			new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig().setApplyAllHighRepJobPolicy());  
	  
	
	Freediver fd= null;
	
	@Before  
    public void setUp() {  
        helper.setUp(); 
        //create an entity instance of Freediver
        try {
        	
        	fd = LogbookDAO.addFreediver("existsId", "name external", "email external", LogbookConstant.FACEBOOK_PLATFORM);
        	
        	
        	System.out.println(fd);
        }catch (Exception e) {
        	
        	
        }
    }  
	  
	@After  
    public void tearDown() {  
        helper.tearDown();  
    }  
	
	@Test
	public void testDiveSession() {
		
		//1) test add dive session expressed as meter, celsius, kg unit 
		DiveSession ds1 = null;
		
		try {
			ds1 = LogbookDAO.addDiveSession(fd.getId(), new Date(100000), 35.6, "mask, lanyard, dive suti 5.5 mm", "Elba Island - margidore", null, "sunny", "katabasis course ssi level 3", 20.0, 5.0, LogbookConstant.DEEP_METER, LogbookConstant.TEMPERATURE_CELSIUS, LogbookConstant.WEIGHT_KILOGRAM);
		} catch (FreediverIDException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(ds1!=null);
		assertTrue(ds1.getDeepAsMeter()==35.6);
		assertTrue(ds1.getDeepAsFeet()==116.79790026200001);
		
		assertTrue(ds1.getWaterTempAsCelsius()==20.0);
		assertTrue(ds1.getWaterTempAsFahrehneit()==68.0);
		
		assertTrue(ds1.getWeightAsKilogram()==5.0);
		assertTrue(ds1.getWeightAsPound()==11.023);
		
		assertTrue(ds1.getEquipment().equals("mask, lanyard, dive suti 5.5 mm"));
		assertTrue(ds1.getLocationDesc().equals("Elba Island - margidore"));
		assertTrue(ds1.getLocationGeoPt()==null);
		assertTrue(ds1.getMeteoDesc().equals("sunny"));
		assertTrue(ds1.getNote().getValue().equals("katabasis course ssi level 3"));
		
		//2) test add dive session expressed as feet, farehnehit, pound unit 
		DiveSession ds2 = null;
		
		try {
			ds2 = LogbookDAO.addDiveSession(fd.getId(), new Date(100000), 116.79, "mask, lanyard, dive suti 5.5 mm", "Elba Island - margidore", null, "sunny", "katabasis course ssi level 3", 68.0, 11.023, LogbookConstant.DEEP_FEET, LogbookConstant.TEMPERATURE_FAHRHENEIT, LogbookConstant.WEIGHT_POUND);
		} catch (FreediverIDException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(ds2!=null);
		assertTrue(ds2.getDeepAsMeter()==35.59759200014239);
		assertTrue(ds2.getDeepAsFeet()==116.79);
		
		assertTrue(ds2.getWaterTempAsCelsius()==20.0);
		assertTrue(ds2.getWaterTempAsFahrehneit()==68.0);
		
		assertTrue(ds2.getWeightAsKilogram()==5.0);
		assertTrue(ds2.getWeightAsPound()==11.023);
		
		assertTrue(ds2.getEquipment().equals("mask, lanyard, dive suti 5.5 mm"));
		assertTrue(ds2.getLocationDesc().equals("Elba Island - margidore"));
		assertTrue(ds2.getLocationGeoPt()==null);
		assertTrue(ds2.getMeteoDesc().equals("sunny"));
		assertTrue(ds2.getNote().getValue().equals("katabasis course ssi level 3"));
		
		//3 test add another dive session
		@SuppressWarnings("unused")
		DiveSession ds3;
		try {
			ds3 = LogbookDAO.addDiveSession(fd.getId(), new Date(200000), 11.79, "mask, lanyard, dive suit 5 mm", "Elba Island - marina di campo", null, "partially cloudy", "katabasis course ssi level 3", 50.0, 11.023, LogbookConstant.DEEP_FEET, LogbookConstant.TEMPERATURE_FAHRHENEIT, LogbookConstant.WEIGHT_POUND);
		} catch (FreediverIDException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//get first TWO divesessions by freedivers page1
		DiveSessionsOfFreeediver dsOfFreePag1 = LogbookDAO.getDiveSessionsByFreediver(fd.getId(), 2,null); 
		List<DiveSession> diveSessions = dsOfFreePag1.getDiveSessions();
		String cursorPag1 = dsOfFreePag1.getCursor();
		
		assertTrue(diveSessions!=null);
		assertTrue(diveSessions.size()==2);
		
		//get second ONE divesessions by freedivers page2
		DiveSessionsOfFreeediver dsOfFreePag2 = LogbookDAO.getDiveSessionsByFreediver(fd.getId(), 2, cursorPag1); 
		List<DiveSession> diveSessions2 = dsOfFreePag2.getDiveSessions();
		String cursorPage2 = dsOfFreePag2.getCursor();
		
		assertTrue(diveSessions2!=null);
		assertTrue(diveSessions2.size()==1);
		assertTrue(cursorPage2 != null);
		
		//test removing freediver also removes all his divesessions
		LogbookDAO.removeFreediver(fd.getId());
		//try to get all divesessions of removed freedivers
		DiveSessionsOfFreeediver dsOfFreeAll = LogbookDAO.getDiveSessionsByFreediver(fd.getId(), 10,null);
		assertTrue(dsOfFreeAll==null);
		
		
		
		
	}
	
	
}
