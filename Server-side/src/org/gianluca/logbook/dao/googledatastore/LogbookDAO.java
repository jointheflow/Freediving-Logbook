package org.gianluca.logbook.dao.googledatastore;




import org.gianluca.logbook.dao.exception.FreediverNotExistsException;
import org.gianluca.logbook.dao.googledatastore.entity.Freediver;
import org.gianluca.logbook.helper.LogbookConstant;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;

import static com.google.appengine.api.datastore.FetchOptions.Builder.*;

import com.google.appengine.api.datastore.Transaction;








public class LogbookDAO {
	
	/*Freediver is saved in the datastore as the following:
	 * Entity --> Freediver
	 * 				key: Id
	 * 				externalId: String
	 * 				externalName: String 
					externalEmail: String
					externalPlatformId: int
					deepUnit :int
					temperatureUnit: int
	 * */
	
	
	public static Freediver getFreediverByExternalId(String externalId, long externalPlatformId) { 
		Freediver freediver = null;
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction tx = datastore.beginTransaction();
		try {
			Filter externalIDFilter = new FilterPredicate ("externalId", FilterOperator.EQUAL, externalId);
			Filter externalPlatformIDFilter = new FilterPredicate("externalPlatformId", FilterOperator.EQUAL, externalPlatformId);
			Filter compFilter= CompositeFilterOperator.and(externalIDFilter, externalPlatformIDFilter);
			
			Query q = new Query("Freediver").setFilter(compFilter);		
			PreparedQuery pq = datastore.prepare(q);
			
			if (pq.countEntities(withLimit(1)) > 0) {
				
				//instantiate a Freediver
				freediver = new Freediver();
				for (Entity result : pq.asIterable()) {
					freediver.setExternalEmail((String) result.getProperty("externalEmail"));
					freediver.setExternalName((String) result.getProperty("externalName"));
					freediver.setExternalId((String) result.getProperty("externalId"));
					freediver.setExternalPlatformId((int)(long)result.getProperty("externalPlatformId"));
					freediver.setDeepUnit((int)(long)result.getProperty("deepUnit"));
					freediver.setTemperatureUnit((int)(long)result.getProperty("temperatureUnit"));
					freediver.setId((Key) result.getKey());
				}
								
				
			}
			
				
			tx.commit();
		} finally {
		    if (tx.isActive()) {
		        tx.rollback();
		    }
		}
		
	     return freediver;
		
	}
	//add freediver user and default settings
	public static Freediver addFreediver(String externalId, String externalName, String externalEmail, int externalPlatformId) {
		Key freediverId = null;
		Freediver freediver = null;
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction tx = datastore.beginTransaction();
			try {
						        
				Entity e_freediver = new Entity("Freediver");
			
				e_freediver.setProperty("externalId", externalId);
				e_freediver.setProperty("externalName", externalName);
				e_freediver.setProperty("externalEmail", externalEmail);
				e_freediver.setProperty("externalPlatformId", externalPlatformId);
				//set default settings
				e_freediver.setProperty("deepUnit", LogbookConstant.DEEP_METER);
				e_freediver.setProperty("temperatureUnit", LogbookConstant.TEMPERATURE_CELSIUS);
				datastore.put(e_freediver);
				freediverId = e_freediver.getKey();
				
				//instantiate a new entity object
				freediver = new Freediver();
				freediver.setExternalEmail(externalEmail);
				freediver.setExternalId(externalId);
				freediver.setExternalName(externalName);
				freediver.setExternalPlatformId(externalPlatformId);
				freediver.setId(freediverId);
				
				tx.commit();
			} finally {
			    if (tx.isActive()) {
			        tx.rollback();
			    }
			}
			
	        	
	        return freediver;
		}
		
	}

