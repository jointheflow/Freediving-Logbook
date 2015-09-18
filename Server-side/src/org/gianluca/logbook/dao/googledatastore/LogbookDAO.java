package org.gianluca.logbook.dao.googledatastore;




import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.gianluca.logbook.dao.exception.DiveSessionIdException;
import org.gianluca.logbook.dao.exception.FreediverIdException;
import org.gianluca.logbook.dao.googledatastore.entity.DiveSession;
import org.gianluca.logbook.dao.googledatastore.entity.DiveSessionsOfFreeediver;
import org.gianluca.logbook.dao.googledatastore.entity.Freediver;
import org.gianluca.logbook.helper.LogbookConstant;




import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;

import static com.google.appengine.api.datastore.FetchOptions.Builder.*;

import com.google.appengine.api.datastore.Transaction;

public class LogbookDAO {
	private static final Logger log = Logger.getLogger(LogbookDAO.class.getName());
	/*Freediver is saved in the datastore as the following:
	 * Entity --> Freediver
	 * 				key: id
	 * 				externalId: String
	 * 				externalName: String 
					externalEmail: String
					externalPlatformId: int
					deepUnit :int
					temperatureUnit: int
					weightUnit: int
	 * */
	
	/*DiveSession is saved in the datastore as the following:
	 *  Entity --> DiveSession
	 *  			id: Key
	 *  			diveDate : Date
					locationDesc: String
					locationGeoPt: GeoPt 
					meteoDesc: String 
					waterTempAsCelsius: double;
					waterTempAsFahrehneit: double;
					deepAsMeter: double;
					deepAsFeet: double;
					equipment: String;
					weightAsKilogram: double;
					weightAsPound: double;
					note: Text;
	 * 
	 * */
	
	/*Get a freediver instance by external id provided by external platform authentication*/
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
					freediver.setWeightUnit((int)(long)result.getProperty("weightUnit"));
					freediver.setId(KeyFactory.keyToString(result.getKey()));
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
				e_freediver.setProperty("weightUnit", LogbookConstant.WEIGHT_KILOGRAM);
				datastore.put(e_freediver);
				freediverId = e_freediver.getKey();
				
				//instantiate a new entity object
				freediver = new Freediver();
				freediver.setExternalEmail(externalEmail);
				freediver.setExternalId(externalId);
				freediver.setExternalName(externalName);
				freediver.setExternalPlatformId(externalPlatformId);
				freediver.setId(KeyFactory.keyToString(freediverId));
				
				tx.commit();
			} finally {
			    if (tx.isActive()) {
			        tx.rollback();
			    }
			}
			
	        	
	        return freediver;
		}
	
	/*remove freediver and all child DiveSession entity and all child Dive entity */
	public static void removeFreediver(String freediverId) throws FreediverIdException{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction tx = datastore.beginTransaction();
		
		try {
			
			//get freediver instance and all child of DiveSession (also Dive)  regardless of kind
			Query ancestorQuery = new Query().setAncestor(KeyFactory.stringToKey(freediverId)).setKeysOnly();
			List<Entity> results = datastore.prepare(ancestorQuery).asList(FetchOptions.Builder.withDefaults());
			for (Entity entity : results) {
				log.info("Delete "+ entity.getKey().toString());
				datastore.delete(entity.getKey());
			}
			
			
			
			 	
			tx.commit();
		}catch (IllegalArgumentException e) {
			log.info(e.getMessage());
			throw new FreediverIdException(e.getMessage());
		} finally {
			if (tx.isActive()) {
		        tx.rollback();
		    }
		}
	    	
	}
	
	/*Add new dive session as a child entity for the freediver key passed as parameter*/
	public static DiveSession addDiveSession(String freediverId, Date diveDate, Double deep, String equipment, String locationDesc, GeoPt locationGeoPt, String meteoDesc, String note, Double waterTemp, Double weight, int deepUnit, int tempUnit, int weightUnit) throws FreediverIdException {
		
		Key diveSessionId = null;
		DiveSession diveSession = null;
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction tx = datastore.beginTransaction();
			try {
				
				Entity e_diveSession = new Entity("DiveSession", KeyFactory.stringToKey(freediverId));
				
				/*Executes deep conversion*/
				if (deep !=null)
					switch (deepUnit) {
						case LogbookConstant.DEEP_METER: {
							e_diveSession.setProperty("deepAsMeter", deep);
							e_diveSession.setProperty("deepAsFeet", (deep*LogbookConstant.METER_AS_FEET));
						}
							break;
						
						case LogbookConstant.DEEP_FEET: {
							e_diveSession.setProperty("deepAsFeet", deep);
							e_diveSession.setProperty("deepAsMeter", (deep/LogbookConstant.METER_AS_FEET));
						}
							break;
							
					}
				
				/*Executes waterTemp conversion*/
				if (waterTemp != null)
					switch (tempUnit) {
						case LogbookConstant.TEMPERATURE_CELSIUS: {
							e_diveSession.setProperty("waterTempAsCelsius", waterTemp);
							e_diveSession.setProperty("waterTempAsFahrehneit", (waterTemp*LogbookConstant.CELSIUS_AS_FAREHN_TIME + LogbookConstant.CELSIUS_AS_FAREHN_ADD));
						}
							break;
						
						case LogbookConstant.TEMPERATURE_FAHRHENEIT: {
							e_diveSession.setProperty("waterTempAsFahrehneit", waterTemp);
							e_diveSession.setProperty("waterTempAsCelsius", (waterTemp - LogbookConstant.CELSIUS_AS_FAREHN_ADD)  / LogbookConstant.CELSIUS_AS_FAREHN_TIME);
						}
							break;
					}
				

				/*Execute weigth conversion*/
				if (weight != null)
					switch (weightUnit) {
					case LogbookConstant.WEIGHT_KILOGRAM: {
						e_diveSession.setProperty("weightAsKilogram", weight);
						e_diveSession.setProperty("weightAsPound", (weight*LogbookConstant.KILOGRAM_AS_POUND));
					}
						break;
					
					case LogbookConstant.WEIGHT_POUND: {
						e_diveSession.setProperty("weightAsPound", weight);
						e_diveSession.setProperty("weightAsKilogram", (weight / LogbookConstant.KILOGRAM_AS_POUND));
					}
						break;
				}
				
				e_diveSession.setProperty("diveDate", diveDate);
				e_diveSession.setProperty("equipment", equipment);
				e_diveSession.setProperty("locationDesc", locationDesc);
				e_diveSession.setProperty("locationGeoPt", locationGeoPt);
				e_diveSession.setProperty("meteoDesc", meteoDesc);
				e_diveSession.setProperty("note", new Text(note));
				
				datastore.put(e_diveSession);
				diveSessionId = e_diveSession.getKey();
				
				//instantiate a new entity object
				diveSession = new DiveSession();
				diveSession.setDeepAsFeet((Double)e_diveSession.getProperty("deepAsFeet"));
				diveSession.setDeepAsMeter((Double)e_diveSession.getProperty("deepAsMeter"));
				
				diveSession.setDiveDate(diveDate);
				diveSession.setEquipment(equipment);
				diveSession.setId(KeyFactory.keyToString(diveSessionId));
				diveSession.setLocationDesc(locationDesc);
				diveSession.setLocationGeoPt(locationGeoPt);
				diveSession.setMeteoDesc(meteoDesc);
				diveSession.setNote(((Text)e_diveSession.getProperty("note")));
				
				diveSession.setWaterTempAsCelsius((Double)e_diveSession.getProperty("waterTempAsCelsius"));
				diveSession.setWaterTempAsFahrehneit((Double)e_diveSession.getProperty("waterTempAsFahrehneit"));
				
				diveSession.setWeightAsKilogram((Double)e_diveSession.getProperty("weightAsKilogram"));
				diveSession.setWeightAsPound((Double)e_diveSession.getProperty("weightAsPound"));
				
				
				tx.commit();
			
			}catch (IllegalArgumentException e) {
				log.info(e.getMessage());
				throw new FreediverIdException(e.getMessage());
				
			
			} finally {
			    if (tx.isActive()) {
			        tx.rollback();
			    }
			}
			
	        	
	        return diveSession;
		
	}
	
	/*Get all dive session owned by the freediver key passed as parameter, starting from the given cursor if exists, with limit result to pagSize*/
	public static DiveSessionsOfFreeediver getDiveSessionsByFreediver(String freediverId, int pageSize, String startCursor) {
		
		List<DiveSession> diveSessions = null;
		DiveSessionsOfFreeediver dsOfFree = null;
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction tx = datastore.beginTransaction();
		
		try {
			//set pageSize limit
			FetchOptions fetchOptions = FetchOptions.Builder.withLimit(pageSize);
			//set startCursor, if exists
			if (startCursor != null) {
			      fetchOptions.startCursor(Cursor.fromWebSafeString(startCursor));
			}
			
			//get all sessions descend from freeDiverId key ancestor
			Query q = new Query("DiveSession").setAncestor(KeyFactory.stringToKey(freediverId)).addSort("diveDate",SortDirection.DESCENDING);		
			PreparedQuery pq = datastore.prepare(q);
			QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
			
			if (!results.isEmpty())  {
				diveSessions = new ArrayList<DiveSession>();
			}
			
			for (Entity entity : results) {
				DiveSession ds = new DiveSession();
				ds.setDeepAsFeet((double)entity.getProperty("deepAsFeet"));
				ds.setDeepAsMeter((double)entity.getProperty("deepAsMeter"));
				ds.setDiveDate((Date)entity.getProperty("diveDate"));
				ds.setEquipment((String)entity.getProperty("equipment"));
				ds.setId(KeyFactory.keyToString(entity.getKey()));
				ds.setLocationDesc((String)entity.getProperty("locationDesc"));
				ds.setLocationGeoPt((GeoPt)entity.getProperty("locationGeoPt"));
				ds.setMeteoDesc((String)entity.getProperty("meteoDesc"));
				ds.setNote((Text)entity.getProperty("note"));
				ds.setWaterTempAsCelsius((double)entity.getProperty("waterTempAsCelsius"));
				ds.setWaterTempAsFahrehneit((double)entity.getProperty("waterTempAsFahrehneit"));
				ds.setWeightAsKilogram((double)entity.getProperty("weightAsKilogram"));
				ds.setWeightAsPound((double)entity.getProperty("weightAsPound"));
				
				
				diveSessions.add(ds);
			     
			}
			
			if (!results.isEmpty())  {
			
				dsOfFree = new DiveSessionsOfFreeediver();
				dsOfFree.setDiveSessions(diveSessions);
				dsOfFree.setCursor(results.getCursor().toWebSafeString());
			}
			
			
				
			tx.commit();
		} finally {
		    if (tx.isActive()) {
		        tx.rollback();
		    }
		}
		
	     return dsOfFree;
	}
	
	/*Update dive session with the parameters passed*/
	public static DiveSession updateDiveSession(String diveSessionId, Date diveDate, Double deep, String equipment, String locationDesc, GeoPt locationGeoPt, String meteoDesc, String note, Double waterTemp, Double weight, int deepUnit, int tempUnit, int weightUnit) throws  DiveSessionIdException {
		
		DiveSession diveSession = null;
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction tx = datastore.beginTransaction();
			try {
				//find entity
				Entity e_diveSession = datastore.get(KeyFactory.stringToKey(diveSessionId));
				
				/*Executes deep conversion*/
				if (deep !=null)
					switch (deepUnit) {
						case LogbookConstant.DEEP_METER: {
							e_diveSession.setProperty("deepAsMeter", deep);
							e_diveSession.setProperty("deepAsFeet", (deep*LogbookConstant.METER_AS_FEET));
						}
							break;
						
						case LogbookConstant.DEEP_FEET: {
							e_diveSession.setProperty("deepAsFeet", deep);
							e_diveSession.setProperty("deepAsMeter", (deep/LogbookConstant.METER_AS_FEET));
						}
							break;
							
					}
				
				/*Executes waterTemp conversion*/
				if (waterTemp != null)
					switch (tempUnit) {
						case LogbookConstant.TEMPERATURE_CELSIUS: {
							e_diveSession.setProperty("waterTempAsCelsius", waterTemp);
							e_diveSession.setProperty("waterTempAsFahrehneit", (waterTemp*LogbookConstant.CELSIUS_AS_FAREHN_TIME + LogbookConstant.CELSIUS_AS_FAREHN_ADD));
						}
							break;
						
						case LogbookConstant.TEMPERATURE_FAHRHENEIT: {
							e_diveSession.setProperty("waterTempAsFahrehneit", waterTemp);
							e_diveSession.setProperty("waterTempAsCelsius", (waterTemp - LogbookConstant.CELSIUS_AS_FAREHN_ADD)  / LogbookConstant.CELSIUS_AS_FAREHN_TIME);
						}
							break;
					}
				

				/*Execute weigth conversion*/
				if (weight != null)
					switch (weightUnit) {
					case LogbookConstant.WEIGHT_KILOGRAM: {
						e_diveSession.setProperty("weightAsKilogram", weight);
						e_diveSession.setProperty("weightAsPound", (weight*LogbookConstant.KILOGRAM_AS_POUND));
					}
						break;
					
					case LogbookConstant.WEIGHT_POUND: {
						e_diveSession.setProperty("weightAsPound", weight);
						e_diveSession.setProperty("weightAsKilogram", (weight / LogbookConstant.KILOGRAM_AS_POUND));
					}
						break;
				}
				
				e_diveSession.setProperty("diveDate", diveDate);
				e_diveSession.setProperty("equipment", equipment);
				e_diveSession.setProperty("locationDesc", locationDesc);
				e_diveSession.setProperty("locationGeoPt", locationGeoPt);
				e_diveSession.setProperty("meteoDesc", meteoDesc);
				if (note !=null)
					e_diveSession.setProperty("note", new Text(note));
				else
					e_diveSession.setProperty("note", null);
				
				datastore.put(e_diveSession);
								
				//instantiate a new entity object
				diveSession = new DiveSession();
				diveSession.setDeepAsFeet((Double)e_diveSession.getProperty("deepAsFeet"));
				diveSession.setDeepAsMeter((Double)e_diveSession.getProperty("deepAsMeter"));
				diveSession.setDiveDate(diveDate);
				diveSession.setEquipment(equipment);
				diveSession.setId(diveSessionId);
				diveSession.setLocationDesc(locationDesc);
				diveSession.setLocationGeoPt(locationGeoPt);
				diveSession.setMeteoDesc(meteoDesc);
				diveSession.setNote(((Text)e_diveSession.getProperty("note")));
				
				diveSession.setWaterTempAsCelsius((Double)e_diveSession.getProperty("waterTempAsCelsius"));
				diveSession.setWaterTempAsFahrehneit((Double)e_diveSession.getProperty("waterTempAsFahrehneit"));
				
				diveSession.setWeightAsKilogram((Double)e_diveSession.getProperty("weightAsKilogram"));
				diveSession.setWeightAsPound((Double)e_diveSession.getProperty("weightAsPound"));
				
				
				tx.commit();
			
			}catch (IllegalArgumentException e) {
				log.info(e.getMessage());
				throw new DiveSessionIdException(e.getMessage());
				
			
			}catch (EntityNotFoundException f) {
				
				log.info(f.getMessage());
				throw new DiveSessionIdException(f.getMessage());
			}
			finally {
			    if (tx.isActive()) {
			        tx.rollback();
			    }
			}
			
	        	
	        return diveSession;
		
	}
	
	/*Remove dive session by key and all dives associated*/
	public static void removeDiveSession(String diveSessionId) throws DiveSessionIdException {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction tx = datastore.beginTransaction();
		
		try {
			
			//get dive session instance and all child of Dive  regardless of kind
			Query ancestorQuery = new Query().setAncestor(KeyFactory.stringToKey(diveSessionId)).setKeysOnly();
			List<Entity> results = datastore.prepare(ancestorQuery).asList(FetchOptions.Builder.withDefaults());
			for (Entity entity : results) {
				log.info("Delete "+ entity.getKey().toString());
				datastore.delete(entity.getKey());
			}
			
			
			
			 	
			tx.commit();
		}catch (IllegalArgumentException e) {
			log.info(e.getMessage());
			throw new DiveSessionIdException(e.getMessage());
		} finally {
			if (tx.isActive()) {
		        tx.rollback();
		    }
		}
	}
	
	
	
}

