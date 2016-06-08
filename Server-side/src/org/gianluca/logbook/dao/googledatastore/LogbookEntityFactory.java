package org.gianluca.logbook.dao.googledatastore;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.gianluca.logbook.dao.googledatastore.entity.Dive;
import org.gianluca.logbook.dao.googledatastore.entity.DiveSession;
import org.gianluca.logbook.dao.googledatastore.entity.Freediver;
import org.gianluca.logbook.helper.LogbookConstant;

import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

/*Instantiates entity object basing on datastore entity type*/
public class LogbookEntityFactory {
	
	/**Creates Dive entity basing on the corresponding datastore entity*/
	public static Dive createDiveFromEntity(Entity entity) {
		Dive dive=null;
		//instantiate a new entity object
		if (entity !=null) {
			dive = new Dive();
			
			dive.setId(KeyFactory.keyToString(entity.getKey()));
			if (entity.getProperty("maxDeepAsFeet") != null)
				dive.setMaxDeepAsFeet((Double)entity.getProperty("maxDeepAsFeet"));
			if (entity.getProperty("maxDeepAsMeter")!=null)
				dive.setMaxDeepAsMeter((Double)entity.getProperty("maxDeepAsMeter"));
			if (entity.getProperty("neutralBuoyancyAsFeet")!=null) 
				dive.setNeutralBuoyancyAsFeet((Double)entity.getProperty("neutralBuoyancyAsFeet"));
			if (entity.getProperty("neutralBuoyancyAsMeter")!=null)														  
				dive.setNeutralBuoyancyAsMeter((Double)entity.getProperty("neutralBuoyancyAsMeter"));
			if (entity.hasProperty("duration")) 
				dive.setDuration(castToInteger(entity.getProperty("duration")));
			if (entity.getProperty("diveTime")!=null)
				dive.setDiveTime(castToInteger(entity.getProperty("diveTime")));
			if (entity.getProperty("equipment")!= null)
				dive.setEquipment((String)entity.getProperty("equipment"));
			if (entity.getProperty("note")!=null)
				dive.setNote(((Text)entity.getProperty("note")));
			if(entity.getProperty("depthWaterTempAsCelsius")!=null)
				dive.setDepthWaterTempAsCelsisus((Double)entity.getProperty("depthWaterTempAsCelsius"));
			if (entity.getProperty("depthWaterTempAsFahrehneit")!=null)
				dive.setDepthWaterTempAsfarheneit((Double)entity.getProperty("depthWaterTempAsFahrehneit"));
			if (entity.getProperty("weightAsKilogram")!=null)
				dive.setWeightAsKilogram((Double)entity.getProperty("weightAsKilogram"));
			if (entity.getProperty("weightAsPound")!=null)
				dive.setWeightAsPound((Double)entity.getProperty("weightAsPound"));
			if (entity.getProperty("diveType")!=null)
				dive.setDiveType((String)entity.getProperty("diveType"));
			
			if (entity.getProperty("customFieldList")!=null) {
				EmbeddedEntity eCustomFieldList = (EmbeddedEntity) entity.getProperty("customFieldList");
				Map<String, Object> customFieldList = eCustomFieldList.getProperties();
				dive.setCustomFieldList(customFieldList);
			}
				
		}	
		return dive;
	}

	/*Creates DiveSession entity basing on the corresponding datastore entity*/
	public static DiveSession createDiveSessionFromEntity(Entity entity) {
		DiveSession diveSession=null;
		if (entity !=null) {
			diveSession = new DiveSession();
			diveSession.setId(KeyFactory.keyToString(entity.getKey()));
			if (entity.getProperty("deepAsFeet")!=null)
				diveSession.setDeepAsFeet((Double)entity.getProperty("deepAsFeet"));
			if (entity.getProperty("deepAsMeter")!=null)
				diveSession.setDeepAsMeter((Double)entity.getProperty("deepAsMeter"));
			if (entity.getProperty("diveDate")!=null)
				diveSession.setDiveDate((Date)entity.getProperty("diveDate"));
			if (entity.getProperty("equipment")!=null)
				diveSession.setEquipment((String)entity.getProperty("equipment"));
			if (entity.getProperty("locationDesc")!=null)
				diveSession.setLocationDesc((String)entity.getProperty("locationDesc"));
			if (entity.getProperty("locationGeoPt")!=null)
				diveSession.setLocationGeoPt((GeoPt)entity.getProperty("locationGeoPt"));
			if (entity.getProperty("meteoDesc")!=null)
				diveSession.setMeteoDesc((String)entity.getProperty("meteoDesc"));
			if (entity.getProperty("note")!=null)
				diveSession.setNote(((Text)entity.getProperty("note")));
			if (entity.getProperty("waterTempAsCelsius")!=null)
				diveSession.setWaterTempAsCelsius((Double)entity.getProperty("waterTempAsCelsius"));
			if (entity.getProperty("waterTempAsFahrehneit")!=null)
				diveSession.setWaterTempAsFahrehneit((Double)entity.getProperty("waterTempAsFahrehneit"));
			if (entity.getProperty("weightAsKilogram")!=null)
				diveSession.setWeightAsKilogram((Double)entity.getProperty("weightAsKilogram"));
			if (entity.getProperty("weightAsPound")!=null)
				diveSession.setWeightAsPound((Double)entity.getProperty("weightAsPound"));
		}
		return diveSession;
	}
	
	/*Creates a Freediver basing on the corresponding datastore entity */
	@SuppressWarnings("unchecked")
	public static Freediver createFreediverFromEntity(Entity entity) {
		Freediver freediver = null;
		if (entity != null) {
			//instantiate a new entity object
			freediver = new Freediver();
			if (entity.getProperty("externalEmail")!=null)
				freediver.setExternalEmail((String)entity.getProperty("externalEmail"));
			if (entity.getProperty("externalId")!=null)
				freediver.setExternalId((String)entity.getProperty("externalId"));
			if (entity.getProperty("externalName")!=null)
				freediver.setExternalName((String)entity.getProperty("externalName"));
			if (entity.getProperty("externalPlatformId")!=null)
				freediver.setExternalPlatformId(castToInteger(entity.getProperty("externalPlatformId")));
			if (entity.getProperty("deepUnit")!=null)
				freediver.setDeepUnit(castToInteger(entity.getProperty("deepUnit")));
			if (entity.getProperty("temperatureUnit")!=null)
				freediver.setTemperatureUnit(castToInteger(entity.getProperty("temperatureUnit")));
			if (entity.getProperty("weightUnit")!=null)
				freediver.setWeightUnit(castToInteger(entity.getProperty("weightUnit")));
			if (entity.getProperty("customFieldListOfDive")!=null) {
				freediver.setCustomFieldListOfDive((ArrayList<String>)entity.getProperty("customFieldListOfDive"));
			}
			
			freediver.setId(KeyFactory.keyToString(entity.getKey()));
			
		}
		return freediver;
	}
	/*Use this method to manage Integer because are thared as Long*/
	public static Integer castToInteger(Object obj_val) {
		if (obj_val.getClass().getSimpleName().equals("Long"))
			return (Integer)(new Long ((Long)obj_val)).intValue();
		else
			return (Integer) obj_val;
					
		
	}
	/*populates a datastore entity Freediver basing on passed parameters*/
	public static void populateEntityFreediver(Entity e_freediver, String externalId, String externalName, String externalEmail, int externalPlatformId) {
		if (e_freediver !=null) {		
			
			e_freediver.setProperty("externalId", externalId);
			e_freediver.setProperty("externalName", externalName);
			e_freediver.setProperty("externalEmail", externalEmail);
			e_freediver.setProperty("externalPlatformId", externalPlatformId);
			//set default settings
			e_freediver.setProperty("deepUnit", LogbookConstant.DEEP_METER);
			e_freediver.setProperty("temperatureUnit", LogbookConstant.TEMPERATURE_CELSIUS);
			e_freediver.setProperty("weightUnit", LogbookConstant.WEIGHT_KILOGRAM);
			
		}
		
	}
	
	public static void updateEntityFreediver(Entity e_freediver, ArrayList<String> customFieldListOfDive) {
		if (e_freediver !=null) {		
			e_freediver.setProperty("customFieldListOfDive", customFieldListOfDive);
		}
		
	}
	
	/*Populates a datastore entity DiveSession basing on passed parameters*/
	public static void populateEntityDiveSession(Entity e_diveSession, Date diveDate, Double deep, String equipment, String locationDesc, GeoPt locationGeoPt, String meteoDesc, String note, Double waterTemp, Double weight, int deepUnit, int tempUnit, int weightUnit) {
		
		if (e_diveSession !=null) {	
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
		}
		
	}
	/*populates Dive entity basing on passed parameters*/
	public static void populateEntityDive(Entity e_dive, Integer diveTime_minute,String diveType, Integer duration_second, String equipment, Double deep, Double neutralBuoyancy, String note, Double weight, Double depthWaterTemp, int deepUnit, int tempUnit, int weightUnit, Map<String, String> customFieldList) {
		if (e_dive !=null) {
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
						e_dive.setProperty("neutralBuoyancyAsMeter", neutralBuoyancy);
						e_dive.setProperty("neutralBuoyancyAsFeet", (neutralBuoyancy*LogbookConstant.METER_AS_FEET));
					}
						break;
					
					case LogbookConstant.DEEP_FEET: {
						e_dive.setProperty("neutralBuoyancyAsFeet", neutralBuoyancy);
						e_dive.setProperty("neutralBuoyancyAsMeter", (neutralBuoyancy/LogbookConstant.METER_AS_FEET));
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
			
			//if custom field exists save in the embedded entity
			if (customFieldList != null) {
				//populate EmbeddedEntity with custom field
				EmbeddedEntity e_customFieldList = new EmbeddedEntity();
				Iterator<String> keyIterator = customFieldList.keySet().iterator();
				while (keyIterator.hasNext()) {
					String keyName = keyIterator.next();
					e_customFieldList.setProperty(keyName, customFieldList.get(keyName));
					
				}
				//set the embedded property
				e_dive.setProperty("customFieldList", e_customFieldList);
			}
			
		}
	}
	
}
