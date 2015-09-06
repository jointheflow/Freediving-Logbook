package org.gianluca.logbook.rest.resource;




import java.util.Date;
import java.util.logging.Logger;

import org.gianluca.logbook.dao.googledatastore.LogbookDAO;
import org.gianluca.logbook.dao.googledatastore.entity.DiveSession;
import org.gianluca.logbook.dto.DiveSessionDto;
import org.gianluca.logbook.dto.LogbookDto;
import org.gianluca.logbook.external.integration.ExternalUser;
import org.gianluca.logbook.external.integration.ExternalUserFactory;
import org.restlet.representation.Representation;
import org.restlet.resource.*; 
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.data.Status;
import org.restlet.ext.json.*;

import com.google.appengine.api.datastore.KeyFactory;
import com.restfb.exception.FacebookOAuthException;

public class DiveSessionAddResource<K> extends ServerResource{
	private static final Logger log = Logger.getLogger(DiveSessionAddResource.class.getName());
	
	
		
	/*Add a new DiveSession for a freediver. Check if token is valid against external platform*/
	@Post
	public Representation addDiveSession(Representation entity) throws ResourceException {
		//create json response
		JsonRepresentation representation = null;
	    Form form = new Form(entity); 
		try {
			 
	        for (Parameter parameter : form) {
	        	log.info("parameter " + parameter.getName());
	   		  	log.info("/" + parameter.getValue());
	        }	
	         
	        // retrieves customer parameters  
		    // "name=value"  
	        String externalToken = form.getFirstValue("external_token");
	        String externalPlatformId = form.getFirstValue("external_platform_id");
	        String freediverId = form.getFirstValue("freediver_id");
	        Date diveDate = new Date(new Long( form.getFirstValue("dive_date")));
			Double deep = new Double(form.getFirstValue("deep"));
			String equipment = form.getFirstValue("equipment"); 
			String location = form.getFirstValue("location");
			String meteo = form.getFirstValue("meteo");
			String note = form.getFirstValue("note");
			Double waterTemp = new Double(form.getFirstValue("water_temp"));
		    Double weight = new Double(form.getFirstValue("weight"));
		    int deepUnit = Integer.parseInt(form.getFirstValue("deep_unit"));
		    int weightUnit = Integer.parseInt(form.getFirstValue("weight_unit"));
		    int tempUnit = Integer.parseInt(form.getFirstValue("temp_unit"));
		    
		    //TODO check if parameters exists and are valid
		     
		   
		    //check token against external platform
			ExternalUserFactory.checkExternalToken(externalToken, Integer.parseInt(externalPlatformId));
		    
		    //add dive session
		    DiveSession ds = LogbookDAO.addDiveSession(KeyFactory.stringToKey(freediverId), diveDate, deep, equipment , location, null, meteo, note, waterTemp, weight, deepUnit, tempUnit, weightUnit);
		    
		    //create result dto
		    DiveSessionDto dsDto = new DiveSessionDto();
			dsDto.setDeepAsFeet(ds.getDeepAsFeet());
			dsDto.setDeepAsMeter(ds.getDeepAsMeter());
			dsDto.setDiveDate(ds.getDiveDate());
			dsDto.setEquipment(ds.getEquipment());
			dsDto.setExternalToken(externalToken);
			dsDto.setId(KeyFactory.keyToString(ds.getId()));
			dsDto.setLocationDesc(ds.getLocationDesc());
			if (ds.getLocationGeoPt() != null) {
				dsDto.setLocationLatitude(ds.getLocationGeoPt().getLatitude());
				dsDto.setLocationLongitude(ds.getLocationGeoPt().getLongitude());
			}
			dsDto.setMeteoDesc(ds.getMeteoDesc());
			dsDto.setMeteoDesc(ds.getNote().getValue());
			dsDto.setWaterTempAsCelsius(ds.getWaterTempAsCelsius());
			dsDto.setWaterTempAsFahrehneit(ds.getWaterTempAsFahrehneit());
			dsDto.setWeightAsKilogram(ds.getWeightAsKilogram());
			dsDto.setWeightAsPound(ds.getWeightAsPound());
		    //return dive dto
		    
			//Set dto status and message
			dsDto.setResult(LogbookDto.RESULT_OK);
			dsDto.setMessage("Dive session added");
			
			
			representation= new JsonRepresentation(dsDto);
			representation.setIndenting(true);
			
			return representation;
			
		}catch (Exception e) {
			//TODO manage exception
			e.printStackTrace();
			//TODO return error representation
			return null;
			
		}finally {
			log.info("end  POST add for DiveSession");
			
		}   
	}  
	 
		
	  
	
}