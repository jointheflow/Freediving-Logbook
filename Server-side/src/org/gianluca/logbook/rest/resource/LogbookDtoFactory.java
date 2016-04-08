package org.gianluca.logbook.rest.resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.gianluca.logbook.dao.googledatastore.entity.Dive;
import org.gianluca.logbook.dao.googledatastore.entity.DiveSession;
import org.gianluca.logbook.dao.googledatastore.entity.DiveSessionsOfFreeediver;
import org.gianluca.logbook.dao.googledatastore.entity.Freediver;
import org.gianluca.logbook.dto.DiveDto;
import org.gianluca.logbook.dto.DiveInputDto;
import org.gianluca.logbook.dto.DiveSessionDto;
import org.gianluca.logbook.dto.DiveSessionInputDto;
import org.gianluca.logbook.dto.FreediverDto;
import org.gianluca.logbook.dto.FreediverInputDto;
import org.gianluca.logbook.external.integration.ExternalUserFactory;
import org.gianluca.logbook.external.integration.PlatformNotManagedException;
import org.gianluca.logbook.helper.LogbookConstant;
import org.gianluca.logbook.rest.exception.WrongParameterException;
import org.restlet.data.Form;

/*Factory used to create Dto from http params and fto from json result*/
public class LogbookDtoFactory {
	public static int REQUEST_ADD = 0;
	public static int REQUEST_UPDATE =1;
	public static int REQUEST_REMOVE = 2;
	public static int REQUEST_GET = 3;
	public static int REQUEST_GET_PUBLISHED = 4;
	public static int REQUEST_FB_PUBLISH = 5;
	
	public static void checkMandatory(String value, String name) throws WrongParameterException {
		if (value==null) throw new WrongParameterException("Parameter "+name+" missing");
	}
	
	public static void checkExternalPlatformId(String externalPlatformId) throws WrongParameterException {
		try {
				
			if (Integer.parseInt(externalPlatformId) < LogbookConstant.FACEBOOK_PLATFORM &&
					Integer.parseInt(externalPlatformId) > LogbookConstant.GOOGLE_PLATFORM) {
				throw new WrongParameterException("Parameter external_platform_id "+externalPlatformId+" wrong value");
			}
		 } catch (NumberFormatException e) {
			  throw new WrongParameterException("Parameter external_platform_id "+externalPlatformId+" wrong value");
		 }
	}
	
	
		public static void checkDivesessionId(String divesessionId) throws WrongParameterException {
		if (divesessionId==null) throw new WrongParameterException("Parameter divesession_id missing");
	}
	
	
	public static void checkDiveId(String diveId) throws WrongParameterException {
		if (diveId==null) throw new WrongParameterException("Parameter dive_id missing");
	}
	
	public static void checkDouble(String doubleValue, String name) throws WrongParameterException {
		try {
			if (doubleValue!=null) new Double(doubleValue);
		
		 }catch (NumberFormatException e) {
			 throw new WrongParameterException("Parameter "+ name +" "+ doubleValue+" wrong value");
		 }
	}
	
	
	public static void checkDate(String aDate, String name) throws WrongParameterException {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(LogbookConstant.DATE_FORMAT);
		    formatter.parse(aDate);
			
		}catch(ParseException e) {
			throw new WrongParameterException("Parameter "+ name + " "+e.getMessage());
			
		}
		
	}
	
	public static void checkInt(String intValue, String name) throws WrongParameterException {
		try {
			if (intValue!=null) new Integer(intValue);
		 }catch (NumberFormatException e) {
			  throw new WrongParameterException("Parameter "+ name +" "+ intValue+" wrong value");
		 }
	}
	
	
	public static void checkTime(String time, String name) throws WrongParameterException{
		checkInt(time, name);
		if (Integer.parseInt(time)<0 || Integer.parseInt(time) >1440) throw new WrongParameterException("Parameter "+ name+"  "+"must be a value between 0 and 1440");
		
	}
	
	public static void checkDuration(String duration, String name) throws WrongParameterException{
		if (duration != null)
			if (Integer.parseInt(duration)<0) throw new WrongParameterException("Parameter "+ name+"  "+"must be a value >=0 ");
		
	}
	
	public static void checkDeepUnit(String deepUnit, String name) throws WrongParameterException {
		checkInt(deepUnit,name);
		if ((new Integer(deepUnit) < LogbookConstant.DEEP_METER) || (new Integer (deepUnit) > LogbookConstant.DEEP_FEET)) throw new WrongParameterException("Parameter  "+ name+"  wrong value");
	}
	
	public static void checkWeightUnit(String weightUnit, String name) throws WrongParameterException {
		checkInt(weightUnit, name);
		if ((new Integer(weightUnit) < LogbookConstant.WEIGHT_KILOGRAM) || (new Integer (weightUnit) > LogbookConstant.WEIGHT_POUND)) throw new WrongParameterException("Parameter "+ name+" wrong value");
		
	}
	
	public static void checkTempUnit(String tempUnit, String name) throws WrongParameterException {
		checkInt(tempUnit, name);
		if ((new Integer(tempUnit) < LogbookConstant.TEMPERATURE_CELSIUS) || (new Integer (tempUnit) > LogbookConstant.TEMPERATURE_FAHRHENEIT)) throw new WrongParameterException("Parameter "+name+" wrong value");
		
	}
	
	
	public static DiveInputDto createDiveInputDtoFromPOSTRequest(Form form,  int requestType) throws WrongParameterException, NumberFormatException, PlatformNotManagedException {
		DiveInputDto diveInput = new DiveInputDto();
		//check and set all parameters
		//basing on the request set ancestor key or id key
		
		if (requestType != REQUEST_GET_PUBLISHED) {
			String externalToken = form.getFirstValue("external_token");
			checkMandatory(externalToken, "external_token");
			diveInput.setExternalToken(externalToken);
			
			String s_externalPlatformId = form.getFirstValue("external_platform_id");
			checkMandatory(s_externalPlatformId, "external_platform_id");
			checkExternalPlatformId(s_externalPlatformId);
			diveInput.setExternalPlatformId(new Integer(s_externalPlatformId));
			
			//check token against external platform
			ExternalUserFactory.checkExternalToken(externalToken, new Integer(s_externalPlatformId));
		}	    
		
		if (requestType == REQUEST_ADD || requestType == REQUEST_GET  || requestType == REQUEST_GET_PUBLISHED) {
			String sessionId = form.getFirstValue("divesession_id");
		    checkMandatory(sessionId, "divesession_id");
		    diveInput.setDiveSessionId(sessionId);
		}
		
		if (requestType == REQUEST_REMOVE || requestType == REQUEST_UPDATE) {
			
			String diveId = form.getFirstValue("dive_id");
		    checkMandatory(diveId, "dive_id");
		    diveInput.setId(diveId);
		}
		
		if (requestType == REQUEST_ADD || requestType == REQUEST_UPDATE) {
			
			
	        String s_timeDive = form.getFirstValue("dive_time");
	        
	        checkMandatory(s_timeDive, "dive_time");
	        checkTime(s_timeDive, "dive_time");
	        Integer diveTime = new Integer(s_timeDive);
	        diveInput.setDiveTime(diveTime);
	        
	        
	        String s_maxDeep = form.getFirstValue("max_depth");
	        if (s_maxDeep !=null){
		        checkDouble(s_maxDeep, "max_depth");
		        Double maxDeep = new Double(s_maxDeep);
		        diveInput.setMaxDeep(maxDeep);
	        }
	        
	        String s_duration = form.getFirstValue("duration");
	        if (s_duration != null) {
		        checkInt(s_duration, "duration");
		        checkDuration(s_duration, "duration");
		        Integer duration = new Integer(s_duration);
		        diveInput.setDuration(duration);
	        }
	        
	        String s_waterTemp = form.getFirstValue("depth_water_temp");
		    if (s_waterTemp!=null) {
		        checkDouble(s_waterTemp, "depth_water_temp");
			    Double waterTemp = new Double(s_waterTemp);
			    diveInput.setWaterTemp(waterTemp);
		    }
		    
	        String s_weight = form.getFirstValue("weight");
			if (s_weight!=null) {
		        checkDouble(s_weight, "weight");
				Double weight = new Double(s_weight);
				diveInput.setWeight(weight);				
			}
			
			String equipment = form.getFirstValue("equipment"); 
			diveInput.setEquipment(equipment);
			
			String note = form.getFirstValue("note");
			diveInput.setNote(note);
			
	        String s_deepUnit = form.getFirstValue("deep_unit");
		    checkMandatory(s_deepUnit, "deep_unit");
		    checkDeepUnit(s_deepUnit, "deep_unit");
		    int deepUnit = Integer.parseInt(s_deepUnit);
		    diveInput.setDeepUnit(deepUnit);
		    
		    String s_weightUnit = form.getFirstValue("weight_unit");
		    checkMandatory(s_weightUnit,"weight_unit");
		    checkWeightUnit(s_weightUnit, "weight_unit");
		    int weightUnit = Integer.parseInt(s_weightUnit);
		    diveInput.setWeightUnit(weightUnit);
		    
		    String s_tempUnit = form.getFirstValue("temp_unit");
		    checkMandatory(s_tempUnit, "temp_unit");
		    checkTempUnit(s_tempUnit, "temp_unit");
		    int tempUnit = Integer.parseInt(s_tempUnit);
		    diveInput.setTempUnit(tempUnit);
		    
		    String s_neutralBuoyance = form.getFirstValue("neutral_buoyance");
		    if (s_neutralBuoyance!=null) {
		    	checkDouble(s_neutralBuoyance, "neutral_buoyance");
			    Double neutralBuoyance = new Double(s_neutralBuoyance);
			    diveInput.setNeutralBuoyance(neutralBuoyance);
		    }
		    
	        String diveType =form.getFirstValue("dive_type");
	        diveInput.setDiveType(diveType);
			
	        String s_depthWaterTemp=form.getFirstValue("depth_water_temp");
	        if (s_depthWaterTemp != null) {
			    checkDouble(s_depthWaterTemp, "depth_water_temp");
		        Double depthWaterTemp = new Double(s_depthWaterTemp);
		        diveInput.setDepthWaterTemp(depthWaterTemp);
	        }  
		}   		   
	    
		return diveInput;
		
	}
	
	public static DiveDto createDiveDtoFromEntity(Dive d) {
		DiveDto dDto = new DiveDto();
		dDto.setMaxDeepAsFeet(d.getMaxDeepAsFeet());
		dDto.setMaxDeepAsMeter(d.getMaxDeepAsMeter());
		dDto.setDiveTime(d.getDiveTime());
		dDto.setEquipment(d.getEquipment());
		dDto.setId(d.getId());
		if (d.getNote()!=null)
			dDto.setNote(d.getNote().getValue());
		dDto.setDepthWaterTempAsCelsius(d.getDepthWaterTempAsCelsisus());
		dDto.setDepthWaterTempAsFahrehneit(d.getDepthWaterTempAsfarheneit());
		dDto.setWeightAsKilogram(d.getWeightAsKilogram());
		dDto.setWeightAsPound(d.getWeightAsPound());
		dDto.setDuration(d.getDuration());
		dDto.setNeutralBuoyanceAsFeet(d.getNeutralBuoyancyAsFeet());
		dDto.setNeutralBuoyanceAsMeter(d.getNeutralBuoyancyAsMeter());
		dDto.setDiveType(d.getDiveType());
		
		return dDto;
	}
	
	public static DiveSessionInputDto createDiveSessionInputDtoFromPOSTRequest(Form form, int requestType) throws WrongParameterException, NumberFormatException, PlatformNotManagedException, ParseException {
		DiveSessionInputDto diveSessionInputDto = new DiveSessionInputDto();
		
		//retrieves and check all parameters
		//-----MANDATORY PARAMETER----//
		String externalToken = form.getFirstValue("external_token");
		checkMandatory(externalToken, "external_token");
		diveSessionInputDto.setExternalToken(externalToken);		
		
      	String externalPlatformId = form.getFirstValue("external_platform_id");
		checkMandatory(externalPlatformId,"external_platform_id");
		checkExternalPlatformId(externalPlatformId);
		diveSessionInputDto.setExternalPlatformId(new Integer(externalPlatformId));
		
		//check token against external platform
		ExternalUserFactory.checkExternalToken(externalToken, Integer.parseInt(externalPlatformId));		
		
		
		if (requestType == REQUEST_ADD) {
        	String freediverId = form.getFirstValue("freediver_id");
            checkMandatory(freediverId, "freediver_id");
            diveSessionInputDto.setFreediverId(freediverId);	
        }
        
        if (requestType == REQUEST_UPDATE || requestType == REQUEST_REMOVE || requestType == REQUEST_FB_PUBLISH) {
        	String divesessionId = form.getFirstValue("divesession_id");
            checkMandatory(divesessionId, "divesession_id");
            diveSessionInputDto.setId(divesessionId);       	
        	
        }
        //OPTIONAL PARAMETER//
        if (requestType == REQUEST_UPDATE || requestType == REQUEST_ADD) {
        
            String s_diveDate = form.getFirstValue("dive_date");
            checkMandatory(s_diveDate, "dive_date");
            checkDate(s_diveDate, "dive_date");
            SimpleDateFormat formatter = new SimpleDateFormat(LogbookConstant.DATE_FORMAT);
    	    Date diveDate = formatter.parse(s_diveDate);
    	    diveSessionInputDto.setDiveDate(diveDate);
    	    
    	    String s_deepUnit = form.getFirstValue("deep_unit");
    	    checkMandatory(s_deepUnit, "deep_unit");
    	    checkDeepUnit(s_deepUnit, "deep_unit");
    	    int deepUnit = Integer.parseInt(s_deepUnit);
    	    diveSessionInputDto.setDeepUnit(deepUnit);
    	    
    	    String s_weightUnit = form.getFirstValue("weight_unit");
    	    checkMandatory(s_weightUnit,"weight_unit");
    	    checkWeightUnit(s_weightUnit, "weight_unit");
    	    int weightUnit = Integer.parseInt(s_weightUnit);
    	    diveSessionInputDto.setWeightUnit(weightUnit);
    	    
    	    String s_tempUnit = form.getFirstValue("temp_unit");
    	    checkMandatory(s_tempUnit, "temp_unit");
    	    checkTempUnit(s_tempUnit, "temp_unit");
    	    int tempUnit = Integer.parseInt(s_tempUnit);   
    	    diveSessionInputDto.setTempUnit(tempUnit);
	    	    
	        String s_deep = form.getFirstValue("deep");
	        if (s_deep !=null) { 
	        	checkDouble(s_deep, "deep");
	        	Double deep = new Double(s_deep);
	        	diveSessionInputDto.setDeep(deep);
	        }
	        
	        String s_waterTemp = form.getFirstValue("water_temp");
		    if (s_waterTemp !=null) { 
		    	checkDouble(s_waterTemp, "water_temp");
		    	Double waterTemp = new Double(s_waterTemp);
		    	diveSessionInputDto.setWaterTemp(waterTemp);
		    }
		    
	        String s_weight = form.getFirstValue("weight");
	        if (s_weight !=null) {
	        	checkDouble(s_weight, "weight");
				Double weight = new Double(s_weight);
				diveSessionInputDto.setWeight(weight);
	        }			      
			
	        
		    String equipment = form.getFirstValue("equipment"); 
		    diveSessionInputDto.setEquipment(equipment);
		    
		    
		    String meteo = form.getFirstValue("meteo");
		    diveSessionInputDto.setMeteoDesc(meteo);
		    
			String note = form.getFirstValue("note");
			diveSessionInputDto.setNote(note);
			
        }		
        
        if (requestType == REQUEST_UPDATE || requestType == REQUEST_ADD || requestType == REQUEST_FB_PUBLISH) {
			String location = form.getFirstValue("location");
		    diveSessionInputDto.setLocationDesc(location);
        }
        
        //manage parameters necessaries for facebook publication
        if (requestType == REQUEST_FB_PUBLISH){
		    
        	String s_userName = form.getFirstValue("userName");
			diveSessionInputDto.setUserName(s_userName);
			
			String s_maxDepth = form.getFirstValue("maxDepth");
	        if (s_maxDepth !=null) {
	        	checkDouble(s_maxDepth, "maxDepth");
				Double maxDepth = new Double(s_maxDepth);
				diveSessionInputDto.setMaxDiveDepth(maxDepth);
	        }
	        
	        String s_maxDuration = form.getFirstValue("maxDuration");
	        diveSessionInputDto.setMaxDiveDuration(s_maxDuration);
	        		
			
			
			
		}	
        
		return diveSessionInputDto;
	    
		
	}
	
	public static DiveSessionDto createDiveSessionDtoFromEntity(DiveSession ds) {
		 //create result dto
		DiveSessionDto dsDto = new DiveSessionDto();
		
		dsDto.setDeepAsFeet(ds.getDeepAsFeet());
		dsDto.setDeepAsMeter(ds.getDeepAsMeter());
		dsDto.setDiveDate(ds.getDiveDate());
		dsDto.setEquipment(ds.getEquipment());
		dsDto.setId(ds.getId());
		dsDto.setLocationDesc(ds.getLocationDesc());
		if (ds.getLocationGeoPt() != null) {
			dsDto.setLocationLatitude(ds.getLocationGeoPt().getLatitude());
			dsDto.setLocationLongitude(ds.getLocationGeoPt().getLongitude());
		}
		dsDto.setMeteoDesc(ds.getMeteoDesc());
		if (ds.getNote()!=null)
			dsDto.setNote(ds.getNote().getValue());
		dsDto.setWaterTempAsCelsius(ds.getWaterTempAsCelsius());
		dsDto.setWaterTempAsFahrehneit(ds.getWaterTempAsFahrehneit());
		dsDto.setWeightAsKilogram(ds.getWeightAsKilogram());
		dsDto.setWeightAsPound(ds.getWeightAsPound());
		
		if (ds.getDives()!=null) {
			List<DiveDto> ListOfDivesDto = new ArrayList<DiveDto>();
			//check all dives and add to dive session using createDiveDtoFromEntity
			for ( int i=0; i< ds.getDives().getDives().size(); i++) {
				Dive d = new Dive();
				d = ds.getDives().getDives().get(i);
				
				DiveDto dDto = createDiveDtoFromEntity(d);
				ListOfDivesDto.add(dDto);
			}
			dsDto.dives=ListOfDivesDto;
		}
		
		return dsDto;
		
	}
	
	public static FreediverInputDto createFreediverInputDtoFromGETRequest(Form form, int requestType) throws WrongParameterException {
		FreediverInputDto freeInputDto = new FreediverInputDto();
		
		if (requestType == REQUEST_REMOVE || requestType == REQUEST_UPDATE) {
			  String freediverId = form.getFirstValue("freediver_id");
		      checkMandatory(freediverId, "freediver_id");
		      freeInputDto.setId(freediverId);
			
		}
		
		String p_externalPlatformId= form.getFirstValue("external_platform_id");
		checkMandatory(p_externalPlatformId,"external_platform_id");
		checkExternalPlatformId(p_externalPlatformId);
		freeInputDto.setExternalPlatformId(new Integer(p_externalPlatformId));
		
		String p_externalToken = form.getFirstValue("external_token");
		checkMandatory(p_externalToken, "external_token");
		freeInputDto.setExternalToken(p_externalToken);
		
		if (requestType== REQUEST_ADD) {
			String p_divePageSize = form.getFirstValue("dive_page_size");
			checkMandatory(p_divePageSize, "dive_page_size");
			checkInt(p_divePageSize, "dive_page_size");
			freeInputDto.setPageSize(new Integer(p_divePageSize));
		}	
		return freeInputDto;
		
	} 
	
	public static FreediverDto createFreediverDtoFromEntity(Freediver fd, DiveSessionsOfFreeediver dsOfFree, String fdStatus, String externalToken) {
		
		FreediverDto fdDto = new FreediverDto();
		fdDto.setStatus(LogbookConstant.SW_VERSION);
		fdDto.status=fdStatus;
		fdDto.externalId= fd.getExternalId();
		//fdDto.externalPlatformId = fd.getExternalPlatformId();
		fdDto.externalUsername = fd.getExternalName();
		fdDto.id = fd.getId();
		//fdDto.deepUnit = fd.getDeepUnit();
		//fdDto.tempUnit = fd.getTemperatureUnit();
		//fdDto.externalToken= externalToken;				
		//add dive session to dto
		if (dsOfFree != null) {
			fdDto.diveSessions = new ArrayList<DiveSessionDto>();
			fdDto.setDiveSessionCursor(dsOfFree.getCursor());
			
			for (DiveSession ds : dsOfFree.getDiveSessions()) {
				DiveSessionDto dsDto = new DiveSessionDto();
				dsDto.setDeepAsFeet(ds.getDeepAsFeet());
				dsDto.setDeepAsMeter(ds.getDeepAsMeter());
				dsDto.setDiveDate(ds.getDiveDate());
				dsDto.setEquipment(ds.getEquipment());
				//dsDto.setExternalToken(externalToken);
				dsDto.setId(ds.getId());
				dsDto.setLocationDesc(ds.getLocationDesc());
				if (ds.getLocationGeoPt() != null) {
					dsDto.setLocationLatitude(ds.getLocationGeoPt().getLatitude());
					dsDto.setLocationLongitude(ds.getLocationGeoPt().getLongitude());
				}
				dsDto.setMeteoDesc(ds.getMeteoDesc());
				if (ds.getNote() != null) dsDto.setNote(ds.getNote().getValue());
				dsDto.setWaterTempAsCelsius(ds.getWaterTempAsCelsius());
				dsDto.setWaterTempAsFahrehneit(ds.getWaterTempAsFahrehneit());
				dsDto.setWeightAsKilogram(ds.getWeightAsKilogram());
				dsDto.setWeightAsPound(ds.getWeightAsPound());
			
				fdDto.diveSessions.add(dsDto);
			}
		}
		
		return fdDto;
		
	}
}
