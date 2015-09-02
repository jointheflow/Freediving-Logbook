package org.gianluca.logbook.test.dao;

import static org.junit.Assert.*;

import java.util.Date;

import org.gianluca.logbook.dao.googledatastore.LogbookDAO;
import org.gianluca.logbook.dao.googledatastore.entity.DiveSession;
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
	public void testAddDiveSession() {
		
		//test dive session expressed as meter, celsius, kg unit 
		DiveSession ds = null;
		
		ds = LogbookDAO.addDiveSession(fd.getId(), new Date(100000), 35.6, "mask, lanyard, dive suti 5.5 mm", "Elba Island - margidore", null, "sunny", "katabasis course ssi level 3", 20.0, 5.0, LogbookConstant.DEEP_METER, LogbookConstant.TEMPERATURE_CELSIUS, LogbookConstant.WEIGHT_KILOGRAM);
		
		assertTrue(ds!=null);
		assertTrue(ds.getDeepAsMeter()==35.6);
		assertTrue(ds.getDeepAsFeet()==116.79790026200001);
		
		assertTrue(ds.getWaterTempAsCelsius()==20.0);
		assertTrue(ds.getWaterTempAsFahrehneit()==68.0);
		
		assertTrue(ds.getWeightAsKilogram()==5.0);
		assertTrue(ds.getWeightAsPound()==11.023);
		
		assertTrue(ds.getEquipment().equals("mask, lanyard, dive suti 5.5 mm"));
		assertTrue(ds.getLocationDesc().equals("Elba Island - margidore"));
		assertTrue(ds.getLocationGeoPt()==null);
		assertTrue(ds.getMeteoDesc().equals("sunny"));
		assertTrue(ds.getNote().getValue().equals("katabasis course ssi level 3"));
		
		//test dive session expressed as feet, farehnehit, pound unit 
		ds = null;
		
		ds = LogbookDAO.addDiveSession(fd.getId(), new Date(100000), 116.79, "mask, lanyard, dive suti 5.5 mm", "Elba Island - margidore", null, "sunny", "katabasis course ssi level 3", 68.0, 11.023, LogbookConstant.DEEP_FEET, LogbookConstant.TEMPERATURE_FAHRHENEIT, LogbookConstant.WEIGHT_POUND);
		
		assertTrue(ds!=null);
		assertTrue(ds.getDeepAsMeter()==35.59759200014239);
		assertTrue(ds.getDeepAsFeet()==116.79);
		
		assertTrue(ds.getWaterTempAsCelsius()==20.0);
		assertTrue(ds.getWaterTempAsFahrehneit()==68.0);
		
		assertTrue(ds.getWeightAsKilogram()==5.0);
		assertTrue(ds.getWeightAsPound()==11.023);
		
		assertTrue(ds.getEquipment().equals("mask, lanyard, dive suti 5.5 mm"));
		assertTrue(ds.getLocationDesc().equals("Elba Island - margidore"));
		assertTrue(ds.getLocationGeoPt()==null);
		assertTrue(ds.getMeteoDesc().equals("sunny"));
		assertTrue(ds.getNote().getValue().equals("katabasis course ssi level 3"));
		
	}
	
	
}
