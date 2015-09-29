package org.gianluca.logbook.dao.googledatastore;




import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.gianluca.logbook.dao.exception.DiveIdException;
import org.gianluca.logbook.dao.exception.DiveSessionIdException;
import org.gianluca.logbook.dao.exception.FreediverIdException;
import org.gianluca.logbook.dao.googledatastore.entity.Dive;
import org.gianluca.logbook.dao.googledatastore.entity.DiveSession;
import org.gianluca.logbook.dao.googledatastore.entity.DiveSessionsOfFreeediver;
import org.gianluca.logbook.dao.googledatastore.entity.DivesOfDiveSession;
import org.gianluca.logbook.dao.googledatastore.entity.Freediver;
import org.gianluca.logbook.helper.LogbookConstant;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.GeoPt;

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
	 *  			id: Key (child of freediver)
	 *  			diveDate : Date
					locationDesc: String
					locationGeoPt: GeoPt 
					meteoDesc: String 
					waterTempAsCelsius: double
					waterTempAsFahrehneit: double
					deepAsMeter: double
					deepAsFeet: double
					equipment: String
					weightAsKilogram: double
					weightAsPound: double
					note: Text
	 */
	 /*Dive is saved in the datastore as the following:
	 * Entity --> 	Dive
	 * 				id: Key (child of DiveSession)
	 * 				diveTime: int
	 * 				diveType: String
	 * 				duration: int
	 * 				equipment: String
	 * 				note: Text
	 * 				maxDeepAsFeet: double
	 * 				maxDeepAsMeter: double
	 * 				neutralBuoyancyAsFeet: double
	 * 				neutralBuoyancyAsMeter: double
	 * 				weightAsKilogram: double
					weightAsPound: double
					depthWaterTempAsCelsius: double
					depthWaterTempAsFahrehneit: double
					
					
	 * 
	 */
	
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
					freediver = LogbookEntityFactory.createFreediver(result);
					
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
				
				
				//instantiate a new entity object
				freediver = LogbookEntityFactory.createFreediver(e_freediver);
								
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
				
				
				//instantiate a new entity object
				diveSession = LogbookEntityFactory.createDiveSession(e_diveSession);
				
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
				DiveSession ds = LogbookEntityFactory.createDiveSession(entity);
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
				diveSession = LogbookEntityFactory.createDiveSession(e_diveSession);
				
				
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
	
	//add a new Dive to the DiveSession which id is passed as parameter
	public static Dive addDive(String divesessionId, Integer diveTime_minute,String diveType, Integer duration_second, String equipment, Double deep, Double neutralBuoyancy, String note, Double weight, Double depthWaterTemp, int deepUnit, int tempUnit, int weightUnit) throws DiveSessionIdException {
		Dive dive = null;
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction tx = datastore.beginTransaction();
			try {
				
				Entity e_dive = new Entity("Dive", KeyFactory.stringToKey(divesessionId));
				
				/*Executes deep conversion*/
				if (deep !=null)
					switch (deepUnit) {
						case LogbookConstant.DEEP_METER: {
							e_dive.setProperty("maxDeepAsMeter", deep);
							e_dive.setProperty("maxDeepAsFeet", (deep*LogbookConstant.METER_AS_FEET));
						}
							break;
						
						case LogbookConstant.DEEP_FEET: {
							e_dive.setProperty("maxDeepAsFeet", deep);
							e_dive.setProperty("maxDeepAsMeter", (deep/LogbookConstant.METER_AS_FEET));
						}
							break;
							
					}
				
				/*Executes deep conversion*/
				if (neutralBuoyancy !=null)
					switch (deepUnit) {
						case LogbookConstant.DEEP_METER: {
							e_dive.setProperty("neutralBuoyancyAsMeter", deep);
							e_dive.setProperty("neutralBuoyancyAsFeet", (deep*LogbookConstant.METER_AS_FEET));
						}
							break;
						
						case LogbookConstant.DEEP_FEET: {
							e_dive.setProperty("neutralBuoyancyAsFeet", deep);
							e_dive.setProperty("neutralBuoyancyAsMeter", (deep/LogbookConstant.METER_AS_FEET));
						}
							break;
							
					}
								
				/*Execute weigth conversion*/
				if (weight != null)
					switch (weightUnit) {
					case LogbookConstant.WEIGHT_KILOGRAM: {
						e_dive.setProperty("weightAsKilogram", weight);
						e_dive.setProperty("weightAsPound", (weight*LogbookConstant.KILOGRAM_AS_POUND));
					}
						break;
					
					case LogbookConstant.WEIGHT_POUND: {
						e_dive.setProperty("weightAsPound", weight);
						e_dive.setProperty("weightAsKilogram", (weight / LogbookConstant.KILOGRAM_AS_POUND));
					}
						break;
				}
				
				/*Executes waterTemp conversion*/
				if (depthWaterTemp != null)
					switch (tempUnit) {
						case LogbookConstant.TEMPERATURE_CELSIUS: {
							e_dive.setProperty("depthWaterTempAsCelsius", depthWaterTemp);
							e_dive.setProperty("depthWaterTempAsFahrehneit", (depthWaterTemp*LogbookConstant.CELSIUS_AS_FAREHN_TIME + LogbookConstant.CELSIUS_AS_FAREHN_ADD));
						}
							break;
						
						case LogbookConstant.TEMPERATURE_FAHRHENEIT: {
							e_dive.setProperty("depthWaterTempAsFahrehneit", depthWaterTemp);
							e_dive.setProperty("depthWaterTempAsCelsius", (depthWaterTemp - LogbookConstant.CELSIUS_AS_FAREHN_ADD)  / LogbookConstant.CELSIUS_AS_FAREHN_TIME);
						}
							break;
					}
				
				
				e_dive.setProperty("diveTime", diveTime_minute);
				e_dive.setProperty("diveType", diveType);
				e_dive.setProperty("duration", duration_second);
				e_dive.setProperty("equipment", equipment);
				e_dive.setProperty("note", new Text(note));
				datastore.put(e_dive);
				
				
				//instantiate a new entity object
				dive = LogbookEntityFactory.createDive(e_dive);
					
				
				tx.commit();
			
			}catch (IllegalArgumentException e) {
				log.info(e.getMessage());
				throw new DiveSessionIdException(e.getMessage());
				
			
			} finally {
			    if (tx.isActive()) {
			        tx.rollback();
			    }
			}
			
	        	
	        return dive;
	}
	
	//update the Dive referenced by id
	public static Dive updateDive(String diveId, Integer diveTime_minute,String diveType, Integer duration_second, String equipment, Double deep, Double neutralBuoyancy, String note, Double weight, Double depthWaterTemp, int deepUnit, int tempUnit, int weightUnit) throws DiveIdException {
		
		Dive dive = null;
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction tx = datastore.beginTransaction();
			try {
				
				//find entity
				Entity e_dive = datastore.get(KeyFactory.stringToKey(diveId));
				
				
				/*Executes deep conversion*/
				if (deep !=null)
					switch (deepUnit) {
						case LogbookConstant.DEEP_METER: {
							e_dive.setProperty("maxDeepAsMeter", deep);
							e_dive.setProperty("maxDeepAsFeet", (deep*LogbookConstant.METER_AS_FEET));
						}
							break;
						
						case LogbookConstant.DEEP_FEET: {
							e_dive.setProperty("maxDeepAsFeet", deep);
							e_dive.setProperty("maxDeepAsMeter", (deep/LogbookConstant.METER_AS_FEET));
						}
							break;
							
					}
				
				/*Executes deep conversion*/
				if (neutralBuoyancy !=null)
					switch (deepUnit) {
						case LogbookConstant.DEEP_METER: {
							e_dive.setProperty("neutralBuoyancyAsMeter", deep);
							e_dive.setProperty("neutralBuoyancyAsFeet", (deep*LogbookConstant.METER_AS_FEET));
						}
							break;
						
						case LogbookConstant.DEEP_FEET: {
							e_dive.setProperty("neutralBuoyancyAsFeet", deep);
							e_dive.setProperty("neutralBuoyancyAsMeter", (deep/LogbookConstant.METER_AS_FEET));
						}
							break;
							
					}
								
				/*Execute weigth conversion*/
				if (weight != null)
					switch (weightUnit) {
					case LogbookConstant.WEIGHT_KILOGRAM: {
						e_dive.setProperty("weightAsKilogram", weight);
						e_dive.setProperty("weightAsPound", (weight*LogbookConstant.KILOGRAM_AS_POUND));
					}
						break;
					
					case LogbookConstant.WEIGHT_POUND: {
						e_dive.setProperty("weightAsPound", weight);
						e_dive.setProperty("weightAsKilogram", (weight / LogbookConstant.KILOGRAM_AS_POUND));
					}
						break;
				}
				
				
				/*Executes waterTemp conversion*/
				if (depthWaterTemp != null)
					switch (tempUnit) {
						case LogbookConstant.TEMPERATURE_CELSIUS: {
							e_dive.setProperty("depthWaterTempAsCelsius", depthWaterTemp);
							e_dive.setProperty("depthWaterTempAsFahrehneit", (depthWaterTemp*LogbookConstant.CELSIUS_AS_FAREHN_TIME + LogbookConstant.CELSIUS_AS_FAREHN_ADD));
						}
							break;
						
						case LogbookConstant.TEMPERATURE_FAHRHENEIT: {
							e_dive.setProperty("depthWaterTempAsFahrehneit", depthWaterTemp);
							e_dive.setProperty("depthWaterTempAsCelsius", (depthWaterTemp - LogbookConstant.CELSIUS_AS_FAREHN_ADD)  / LogbookConstant.CELSIUS_AS_FAREHN_TIME);
						}
							break;
					}
				
				e_dive.setProperty("diveTime", diveTime_minute);
				e_dive.setProperty("diveType", diveType);
				e_dive.setProperty("duration", duration_second);
				e_dive.setProperty("equipment", equipment);
				if (note !=null)
					e_dive.setProperty("note", new Text(note));
				else
					e_dive.setProperty("note", null);
				
				datastore.put(e_dive);
				
				
				//instantiate a new entity object
				dive = LogbookEntityFactory.createDive(e_dive);
				
				tx.commit();
			
			}catch (IllegalArgumentException | EntityNotFoundException e) {
				log.info(e.getMessage());
				throw new DiveIdException(e.getMessage());
				
			
			} finally {
			    if (tx.isActive()) {
			        tx.rollback();
			    }
			}
			
	        	
	        return dive;
	}
	
	//remove the Dive referenced by id
	public static void removeDive(String diveId) throws DiveIdException {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction tx = datastore.beginTransaction();
		
		try {
			
			//get dive session instance and all child of Dive  regardless of kind
			Query ancestorQuery = new Query().setAncestor(KeyFactory.stringToKey(diveId)).setKeysOnly();
			List<Entity> results = datastore.prepare(ancestorQuery).asList(FetchOptions.Builder.withDefaults());
			for (Entity entity : results) {
				log.info("Delete "+ entity.getKey().toString());
				datastore.delete(entity.getKey());
			}
			
			
			
			 	
			tx.commit();
		}catch (IllegalArgumentException e) {
			log.info(e.getMessage());
			throw new DiveIdException(e.getMessage());
		} finally {
			if (tx.isActive()) {
		        tx.rollback();
		    }
		}
	}

	/*Get all dives of session by the session key passed as parameter*/
	public static DivesOfDiveSession getDivesBySession(String diveSessionId){
		
		List<Dive> dives = null;
		DivesOfDiveSession dOfSession = null;
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction tx = datastore.beginTransaction();
		
		try {
			
			//get all dives descend from diveSessionId key ancestor
			Query q = new Query("Dive").setAncestor(KeyFactory.stringToKey(diveSessionId));//addSort("diveTime",SortDirection.DESCENDING);		
			PreparedQuery pq = datastore.prepare(q);		
			
			if (pq.countEntities(withLimit(100))>0) dives = new ArrayList<Dive>();		
			
			for (Entity entity : pq.asIterable()) {
				Dive d = LogbookEntityFactory.createDive(entity);
				
				dives.add(d);
			     
			}
			
			
			if (pq.countEntities(withLimit(100))>0)  {
			
				dOfSession = new DivesOfDiveSession();
				dOfSession.setDives(dives);
				
			}
			
			
				
			tx.commit();
		} finally {
		    if (tx.isActive()) {
		        tx.rollback();
		    }
		}
		
	     return dOfSession;
	}
	
	
	
	
}

