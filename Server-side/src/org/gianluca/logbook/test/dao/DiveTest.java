package org.gianluca.logbook.test.dao;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.gianluca.logbook.dao.exception.DiveIdException;
import org.gianluca.logbook.dao.exception.DiveSessionIdException;
import org.gianluca.logbook.dao.exception.FreediverIdException;
import org.gianluca.logbook.dao.googledatastore.LogbookDAO;
import org.gianluca.logbook.dao.googledatastore.entity.Dive;
import org.gianluca.logbook.dao.googledatastore.entity.DiveSession;
import org.gianluca.logbook.dao.googledatastore.entity.DiveSessionsOfFreeediver;
import org.gianluca.logbook.dao.googledatastore.entity.DivesOfDiveSession;
import org.gianluca.logbook.dao.googledatastore.entity.Freediver;
import org.gianluca.logbook.helper.LogbookConstant;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class DiveTest {

	private final LocalServiceTestHelper helper =  
			new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig().setApplyAllHighRepJobPolicy());  
	  
	
	Freediver fd= null;
	DiveSession ds = null;
	
	@Before  
    public void setUp() {  
        helper.setUp(); 
        //create an entity instance of Freediver and associeted a DiveSession to his
        try {
        	
        	fd = LogbookDAO.addFreediver("existsId", "name external", "email external", LogbookConstant.FACEBOOK_PLATFORM);
        	ds = LogbookDAO.addDiveSession(fd.getId(), new Date(100000), 35.6, "mask, lanyard, dive suti 5.5 mm", "Elba Island - margidore", null, "sunny", "katabasis course ssi level 3", 20.0, 5.0, LogbookConstant.DEEP_METER, LogbookConstant.TEMPERATURE_CELSIUS, LogbookConstant.WEIGHT_KILOGRAM);
        	
        	System.out.println(fd);
        }catch (Exception e) {
        	
        	
        }
    }  
	  
	@After  
    public void tearDown() {  
        helper.tearDown();  
    }  
	
	@Test
	public void testDive() {
		
		//first test empty dive session
		//get all dives of divesession 
		DivesOfDiveSession emptySession = LogbookDAO.getDivesBySession(ds.getId()); 
		
				
				
		assertTrue(emptySession==null);
		
				
		
		//1) test add dive expressed as meter, celsius, kg unit 
		Dive d1 = null;
		
		try {
			d1 = LogbookDAO.addDive(ds.getId(), 605 /*10:05*/, "free",180 /*3 minutes*/, "mask, lanyard, dive suti 5.5 mm",20.0, 10.0, "Tuffo di riscaldamento", 5.0, 20.0, LogbookConstant.DEEP_METER, LogbookConstant.TEMPERATURE_CELSIUS, LogbookConstant.WEIGHT_KILOGRAM);
		} catch (DiveSessionIdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(d1!=null);
		assertTrue(d1.getMaxDeepAsMeter()==20.0);
		//assertTrue(d1.getDeepAsFeet()==65.61679790000001);
		
		assertTrue(d1.getNeutralBuoyancyAsMeter()==20.0);
		assertTrue(d1.getWeightAsKilogram()==5.0);
		assertTrue(d1.getWeightAsPound()==11.023);
		
		assertTrue(d1.getEquipment().equals("mask, lanyard, dive suti 5.5 mm"));
		assertTrue(d1.getDiveTime()==605);
		assertTrue(d1.getDuration()==180);
		assertTrue(d1.getDepthWaterTempAsCelsisus()==20.0);
		assertTrue(d1.getDepthWaterTempAsfarheneit()==68.0);
		
		assertTrue(d1.getNote().getValue().equals("Tuffo di riscaldamento"));
		
		//2) test add dive  expressed as feet, farehnehit, pound unit 
		Dive d2 = null;
		
		try {
			d2 = LogbookDAO.addDive(ds.getId(), 606 /*10:06*/, "constant", 120 /*2 minute*/, "mask", 116.79, 10.0, "katabasis course ssi level 3",  11.023, 68.0, LogbookConstant.DEEP_FEET, LogbookConstant.TEMPERATURE_FAHRHENEIT, LogbookConstant.WEIGHT_POUND);
		} catch (DiveSessionIdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(d2!=null);
		assertTrue(d2.getMaxDeepAsMeter()==35.59759200014239);
		assertTrue(d2.getMaxDeepAsFeet()==116.79);
		
		assertTrue(d2.getWeightAsKilogram()==5.0);
		assertTrue(d2.getWeightAsPound()==11.023);
		
		assertTrue(d2.getEquipment().equals("mask"));
		
		assertTrue(d2.getNote().getValue().equals("katabasis course ssi level 3"));
		
		//3 test add another dive 
		@SuppressWarnings("unused")
		Dive d3;
		try {
			d3 = LogbookDAO.addDive(ds.getId(), 607 /*10:07*/, "constant", 120 /*2 minute*/, "mask", 116.79, 10.0, "katabasis course ssi level 3",  11.023, 20.0, LogbookConstant.DEEP_FEET, LogbookConstant.TEMPERATURE_CELSIUS, LogbookConstant.WEIGHT_POUND);
		} catch (DiveSessionIdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//get all dives of divesession 
		DivesOfDiveSession dOfDsPag1 = LogbookDAO.getDivesBySession(ds.getId()); 
		List<Dive> dives = dOfDsPag1.getDives();
		
		
		assertTrue(dives!=null);
		assertTrue(dives.size()==3);
		/*check dive 1
		 * 
		 */
		
		Dive dr1= dives.get(2);//the lastone but is in ascending order
		assertTrue(dr1!=null);
		assertTrue(dr1.getMaxDeepAsMeter()==20.0);
		//assertTrue(d1.getDeepAsFeet()==65.61679790000001);
		
		assertTrue(dr1.getNeutralBuoyancyAsMeter()==20.0);
		assertTrue(dr1.getWeightAsKilogram()==5.0);
		assertTrue(dr1.getWeightAsPound()==11.023);
		
		assertTrue(dr1.getEquipment().equals("mask, lanyard, dive suti 5.5 mm"));
		assertTrue(dr1.getDiveTime()==605);
		assertTrue(dr1.getDuration()==180);
		
		
		assertTrue(dr1.getNote().getValue().equals("Tuffo di riscaldamento"));
		
		
		
		
		//test remove dr 1
		try {
			LogbookDAO.removeDive(dr1.getId());
			//count number of dives
			DivesOfDiveSession newDivesList = LogbookDAO.getDivesBySession(ds.getId());
			assertTrue(newDivesList.getDives().size()==2);
			
		} catch (DiveIdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void updateDiveSession() {
		//1) test update dive session expressed as meter, celsius, kg unit 
		DiveSession ds1 = null;
		
		try {
			ds1 = LogbookDAO.addDiveSession(fd.getId(), new Date(100000), 35.6, "mask, lanyard, dive suti 5.5 mm", "Elba Island - margidore", null, "sunny", "katabasis course ssi level 3", 20.0, 5.0, LogbookConstant.DEEP_METER, LogbookConstant.TEMPERATURE_CELSIUS, LogbookConstant.WEIGHT_KILOGRAM);
		} catch (FreediverIdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		DiveSession ds2 = null;
		try {
			ds2 = LogbookDAO.updateDiveSession(ds1.getId(), new Date(100000), 35.6, "mask", null, null, "sunny", null, null, null, LogbookConstant.DEEP_METER, LogbookConstant.TEMPERATURE_CELSIUS, LogbookConstant.WEIGHT_KILOGRAM);
		} catch (DiveSessionIdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(ds2!=null);
		assertTrue(ds2.getDeepAsMeter()==35.6);
		assertTrue(ds2.getDeepAsFeet()==116.79790026200001);
		
		assertTrue(ds2.getWaterTempAsCelsius()==20.0);
		assertTrue(ds2.getWaterTempAsFahrehneit()==68.0);
		
		assertTrue(ds2.getWeightAsKilogram()==5.0);
		assertTrue(ds2.getWeightAsPound()==11.023);
		
		assertTrue(ds2.getEquipment().equals("mask"));
		assertTrue(ds2.getLocationDesc()==null);
		assertTrue(ds2.getLocationGeoPt()==null);
		assertTrue(ds2.getMeteoDesc().equals("sunny"));
		assertTrue(ds2.getNote()==null);
		
	}
	
	
	
}
