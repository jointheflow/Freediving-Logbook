package org.gianluca.logbook.dao.googledatastore;

import java.util.Date;

import org.gianluca.logbook.dao.googledatastore.entity.Dive;
import org.gianluca.logbook.dao.googledatastore.entity.DiveSession;
import org.gianluca.logbook.dao.googledatastore.entity.Freediver;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

/*Instantiates entity object basing on datastore entity type*/
public class LogbookEntityFactory {
	
	/**Creates Dive entity basing on the corresponding datastore entity*/
	public static Dive createDive(Entity entity) {
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
		}	
		return dive;
	}

	/*Creates DiveSession entity basing on the corresponding datastore entity*/
	public static DiveSession createDiveSession(Entity entity) {
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
	public static Freediver createFreediver(Entity entity) {
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
}
