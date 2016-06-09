package org.gianluca.logbook.test.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gianluca.logbook.dao.exception.DiveIdException;
import org.gianluca.logbook.dao.exception.DiveSessionIdException;
import org.gianluca.logbook.dao.googledatastore.LogbookDAO;
import org.gianluca.logbook.dao.googledatastore.entity.Dive;
import org.gianluca.logbook.dao.googledatastore.entity.DiveSession;
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
        	//add 2 custom field
        	
			ArrayList<String> customFieldListOfDive = new ArrayList<String>();
        	customFieldListOfDive.add("custom_1");
        	customFieldListOfDive.add("custom_2");
        	LogbookDAO.updateFreediverPreferences(fd.getId(), customFieldListOfDive);
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
		DivesOfDiveSession emptySession = null;
		try {
			emptySession = LogbookDAO.getDiveSession(ds.getId()).getDives();
		} catch (DiveSessionIdException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
				
				
		assertTrue(emptySession==null);
		
				
		
		//1) test add dive expressed as meter, celsius, kg unit 
		Dive d1 = null;
		
		try {
			Map<String,String> customFieldListOfDiveMap = new HashMap<String, String>();
			customFieldListOfDiveMap.put("custom_1", "value_custom_1");
			customFieldListOfDiveMap.put("custom_2", "value_custom_2");
			customFieldListOfDiveMap.put("custom_3", "value_custom_3");
			
			
			d1 = LogbookDAO.addDive(ds.getId(), 605 /*10:05*/, "free",180 /*3 minutes*/, "mask, lanyard, dive suti 5.5 mm",20.0, 10.0, "Tuffo di riscaldamento", 5.0, 20.0, LogbookConstant.DEEP_METER, LogbookConstant.TEMPERATURE_CELSIUS, LogbookConstant.WEIGHT_KILOGRAM, customFieldListOfDiveMap);
		} catch (DiveSessionIdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(d1!=null);
		assertTrue(d1.getMaxDeepAsMeter()==20.0);
		//assertTrue(d1.getDeepAsFeet()==65.61679790000001);
		
		assertTrue(d1.getNeutralBuoyancyAsMeter()==10.0);
		assertTrue(d1.getWeightAsKilogram()==5.0);
		assertTrue(d1.getWeightAsPound()==11.023);
		
		assertTrue(d1.getEquipment().equals("mask, lanyard, dive suti 5.5 mm"));
		assertTrue(d1.getDiveTime()==605);
		assertTrue(d1.getDuration()==180);
		assertTrue(d1.getDepthWaterTempAsCelsisus()==20.0);
		assertTrue(d1.getDepthWaterTempAsfarheneit()==68.0);
		
		assertTrue(d1.getNote().getValue().equals("Tuffo di riscaldamento"));
		
		assertTrue(d1.getCustomFieldList().get("custom_1").equals("value_custom_1"));
		assertTrue(d1.getCustomFieldList().get("custom_2").equals("value_custom_2"));
		assertTrue(d1.getCustomFieldList().size()==3);
		
		//2) test add dive  expressed as feet, farehnehit, pound unit 
		Dive d2 = null;
		
		try {
			d2 = LogbookDAO.addDive(ds.getId(), 606 /*10:06*/, "constant", 120 /*2 minute*/, "mask", 116.79, 10.0, "katabasis course ssi level 3",  11.023, 68.0, LogbookConstant.DEEP_FEET, LogbookConstant.TEMPERATURE_FAHRHENEIT, LogbookConstant.WEIGHT_POUND, null);
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
		assertTrue(d2.getCustomFieldList()==null);
		
		//3 test add another dive 
		@SuppressWarnings("unused")
		Dive d3;
		try {
			d3 = LogbookDAO.addDive(ds.getId(), 607 /*10:07*/, "constant",120 /*2 minute*/, "mask", 116.79, 10.0, "katabasis course ssi level 3",  11.023, 20.0, LogbookConstant.DEEP_FEET, LogbookConstant.TEMPERATURE_CELSIUS, LogbookConstant.WEIGHT_POUND, null);
			//System.out.println(d3);
		} catch (DiveSessionIdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//get all dives of divesession 
		DivesOfDiveSession dOfDsPag1 = null;
		try {
			dOfDsPag1 = LogbookDAO.getDiveSession(ds.getId()).getDives();
		} catch (DiveSessionIdException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		List<Dive> dives = dOfDsPag1.getDives();
		
		
		assertTrue(dives!=null);
		assertTrue(dives.size()==3);
		/*check dive 1
		 * 
		 */
		
		Dive dr1= dives.get(2);//the lastone but is in ascending order
		assertTrue(dr1!=null);
		//assertTrue(dr1.getMaxDeepAsMeter()==20.0);
		assertTrue(dr1.getMaxDeepAsFeet()== 116.79);
		
		assertTrue(dr1.getNeutralBuoyancyAsMeter()!=10.0);
		assertTrue(dr1.getWeightAsKilogram()==5.0);
		assertTrue(dr1.getWeightAsPound()==11.023);
		
		assertTrue(dr1.getEquipment().equals("mask"));
		assertTrue(dr1.getDiveTime()==607);
		assertTrue(dr1.getDuration()==120);
		
		
		assertTrue(dr1.getNote().getValue().equals("katabasis course ssi level 3"));
		
		
		
		
		//test remove dr 1
		try {
			LogbookDAO.removeDive(dr1.getId());
			//count number of dives
			DivesOfDiveSession newDivesList = LogbookDAO.getDiveSession(ds.getId()).getDives();
			assertTrue(newDivesList.getDives().size()==2);
			
		} catch (DiveIdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DiveSessionIdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//update d2
		try {
			Map<String,String> customFieldListOfDiveMap = new HashMap<String, String>();
			customFieldListOfDiveMap.put("custom_1", "value_custom_1");
			customFieldListOfDiveMap.put("custom_2", "value_custom_2");
			
			Dive d4= LogbookDAO.updateDive(d2.getId(), 608, "constant", 120, null, 30.0, 10.0, "nessuna", 5.0, 15.0, 0, 0, 0, customFieldListOfDiveMap);
			assertTrue(d4.getId().equals(d2.getId()));
			assertTrue(d4.getDuration()==120);
			assertTrue(d4.getDiveTime()==608);
			assertTrue(d4.getEquipment()==null);
			
			assertTrue(d4.getCustomFieldList().size()==2);
		} catch (DiveIdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
}
