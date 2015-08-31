package org.gianluca.logbook.test.dao;

import static org.junit.Assert.*;

import org.gianluca.logbook.dao.exception.FreediverNotExistsException;
import org.gianluca.logbook.dao.googledatastore.LogbookDAO;
import org.gianluca.logbook.dao.googledatastore.entity.Freediver;
import org.gianluca.logbook.helper.PlatformConstant;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;



import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class FreediverTest {

	private final LocalServiceTestHelper helper =  
			new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig().setApplyAllHighRepJobPolicy());  
	  
	
	
	@Before  
    public void setUp() {  
        helper.setUp(); 
        //create an entity instance of Freediver
        try {
        	Freediver fd= null;
        	fd = LogbookDAO.addFreediver("existsId", "name external", "email external", PlatformConstant.FACEBOOK_PLATFORM);
        	System.out.println(fd);
        }catch (Exception e) {
        	
        	
        }
    }  
	  
	@After  
    public void tearDown() {  
        helper.tearDown();  
    }  
	
	@Test
	public void testNotExistentFreeDiver() {
		
		//get that does not exixts Freediver by facebookId
		String externalIdNotExist = "notExistsId";
		Freediver fd = null;
		try {
			fd = LogbookDAO.getFreediverByExternalId(externalIdNotExist, 0);
			assertTrue(fd==null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}
	
	@Test
	public void testExistentFreeDiver() {
		
		//get that does not exixts Freediver by facebookId
		String externalIdNotExist = "existsId";
		Freediver fd = null;
		try {
			fd = LogbookDAO.getFreediverByExternalId(externalIdNotExist, PlatformConstant.FACEBOOK_PLATFORM);
			assertTrue(fd!=null);
			assertTrue(fd.getExternalEmail().equals("email external"));
			assertTrue(fd.getExternalId().equals("existsId"));
			assertTrue(fd.getExternalName().equals("name external"));
			assertTrue(fd.getExternalPlatformId()==PlatformConstant.FACEBOOK_PLATFORM);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			
		}
	}
}
