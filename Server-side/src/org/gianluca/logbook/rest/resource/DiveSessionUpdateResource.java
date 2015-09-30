package org.gianluca.logbook.rest.resource;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import org.gianluca.logbook.dao.exception.DiveSessionIdException;
import org.gianluca.logbook.dao.googledatastore.LogbookDAO;
import org.gianluca.logbook.dao.googledatastore.entity.DiveSession;
import org.gianluca.logbook.dto.DiveSessionDto;
import org.gianluca.logbook.dto.LogbookDto;
import org.gianluca.logbook.external.integration.ExternalUserFactory;
import org.gianluca.logbook.external.integration.PlatformNotManagedException;
import org.gianluca.logbook.helper.LogbookConstant;
import org.gianluca.logbook.rest.exception.WrongParameterException;
import org.restlet.representation.Representation;
import org.restlet.resource.*; 
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.data.Status;
import org.restlet.ext.json.*;

import com.restfb.exception.FacebookOAuthException;


public class DiveSessionUpdateResource<K> extends ServerResource implements ILogbookResource{
	private static final Logger log = Logger.getLogger(DiveSessionUpdateResource.class.getName());
	
	
		
	/*Update a new DiveSession for a freediver. Check if token is valid against external platform*/
	@Post
	public Representation updateDiveSession(Representation entity) throws ResourceException {
		
		log.info("start POST updateDiveSession() DiveSessionUpdateResource)");
		//create json response
		JsonRepresentation representation = null;
	    Form form = new Form(entity); 
	   
		try {
			 
	        for (Parameter parameter : form) {
	        	
	        	log.info("parameter " + parameter.getName());
	   		  	log.info("/" + parameter.getValue());
	        }	
	         
	        //retrieves and check all parameters
	        String s_diveDate = form.getFirstValue("dive_date");
	        checkMandatory(s_diveDate, "dive_date");
	        checkDate(s_diveDate, "dive_date");
	        SimpleDateFormat formatter = new SimpleDateFormat(LogbookConstant.DATE_FORMAT);
		    Date diveDate = formatter.parse(s_diveDate);
		    
		    String externalToken = form.getFirstValue("external_token");
			checkMandatory(externalToken, "external_token");
			
	      	String externalPlatformId = form.getFirstValue("external_platform_id");
			checkMandatory(externalPlatformId,"external_platform_id");
			checkExternalPlatformId(externalPlatformId);
			
			String divesessionId = form.getFirstValue("divesession_id");
	        checkMandatory(divesessionId, "divesession_id");
	        	        
	        String s_deep = form.getFirstValue("deep");
	        checkDouble(s_deep, "deep");
	        Double deep = new Double(s_deep);
	        
	        String s_waterTemp = form.getFirstValue("water_temp");
		    checkDouble(s_waterTemp, "water_temp");
		    Double waterTemp = new Double(s_waterTemp);
		    
	        String s_weight = form.getFirstValue("weight");
			checkDouble(s_weight, "weight");
			Double weight = new Double(s_weight);
	        
			       
			String s_deepUnit = form.getFirstValue("deep_unit");
		    checkMandatory(s_deepUnit, "deep_unit");
		    checkDeepUnit(s_deepUnit, "deep_unit");
		    int deepUnit = Integer.parseInt(s_deepUnit);
		    
		    String s_weightUnit = form.getFirstValue("weight_unit");
		    checkMandatory(s_weightUnit,"weight_unit");
		    checkWeightUnit(s_weightUnit, "weight_unit");
		    int weightUnit = Integer.parseInt(s_weightUnit);
		    
		    String s_tempUnit = form.getFirstValue("temp_unit");
		    checkMandatory(s_tempUnit, "temp_unit");
		    checkTempUnit(s_tempUnit, "temp_unit");
		    int tempUnit = Integer.parseInt(s_tempUnit);   
		       
		    String equipment = form.getFirstValue("equipment"); 
			String location = form.getFirstValue("location");
			String meteo = form.getFirstValue("meteo");
			String note = form.getFirstValue("note");
		    		   
		    //check token against external platform
			ExternalUserFactory.checkExternalToken(externalToken, Integer.parseInt(externalPlatformId));
		    
		    //update dive session
		    DiveSession ds = LogbookDAO.updateDiveSession(divesessionId, diveDate, deep, equipment , location, null, meteo, note, waterTemp, weight, deepUnit, tempUnit, weightUnit);
		    
		    //create result dto
		    DiveSessionDto dsDto = new DiveSessionDto();
			dsDto.setDeepAsFeet(ds.getDeepAsFeet());
			dsDto.setDeepAsMeter(ds.getDeepAsMeter());
			dsDto.setDiveDate(ds.getDiveDate());
			dsDto.setEquipment(ds.getEquipment());
			dsDto.setExternalToken(externalToken);
			dsDto.setId(ds.getId());
			dsDto.setLocationDesc(ds.getLocationDesc());
			if (ds.getLocationGeoPt() != null) {
				dsDto.setLocationLatitude(ds.getLocationGeoPt().getLatitude());
				dsDto.setLocationLongitude(ds.getLocationGeoPt().getLongitude());
			}
			dsDto.setMeteoDesc(ds.getMeteoDesc());
			dsDto.setNote(ds.getNote().getValue());
			dsDto.setWaterTempAsCelsius(ds.getWaterTempAsCelsius());
			dsDto.setWaterTempAsFahrehneit(ds.getWaterTempAsFahrehneit());
			dsDto.setWeightAsKilogram(ds.getWeightAsKilogram());
			dsDto.setWeightAsPound(ds.getWeightAsPound());
		    //return dive dto
		    
			//Set dto status and message
			dsDto.setResult(LogbookDto.RESULT_OK);
			dsDto.setMessage("Dive session updated");
			
			
			representation= new JsonRepresentation(dsDto);
			representation.setIndenting(true);
			
			return representation;
			
		}catch (FacebookOAuthException e_oa) {
			e_oa.printStackTrace();
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			ErrorResource error = new ErrorResource();
			error.setErrorCode(ErrorResource.WRONG_OAUTH_TOKEN);
			error.setErrorMessage(e_oa.getMessage());
			JsonRepresentation errorRepresentation = new JsonRepresentation(error);
			return errorRepresentation;
		}catch (PlatformNotManagedException e_pnm) {
			e_pnm.printStackTrace();
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			ErrorResource error = new ErrorResource();
			error.setErrorCode(ErrorResource.PLATFORM_NOT_MANAGED_ERROR);
			error.setErrorMessage(e_pnm.getMessage());
			JsonRepresentation errorRepresentation = new JsonRepresentation(error);
			return errorRepresentation;
		
		}catch (NumberFormatException e_ne) {
			e_ne.printStackTrace();
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			ErrorResource error = new ErrorResource();
			error.setErrorCode(ErrorResource.NUMBER_FORMAT_ERROR);
			error.setErrorMessage(e_ne.getMessage());
			JsonRepresentation errorRepresentation = new JsonRepresentation(error);
			return errorRepresentation;
			
		}catch (WrongParameterException e_wp) {
			e_wp.printStackTrace();
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			ErrorResource error = new ErrorResource();
			error.setErrorCode(ErrorResource.WRONG_PARAMETER_ERROR);
			error.setErrorMessage(e_wp.getMessage());
			JsonRepresentation errorRepresentation = new JsonRepresentation(error);
			return errorRepresentation;	
			
		}catch(DiveSessionIdException a_e) {
			a_e.printStackTrace();
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			ErrorResource error = new ErrorResource();
			error.setErrorCode(ErrorResource.DIVESESSION_ID_ERROR);
			error.setErrorMessage(a_e.getMessage());
			JsonRepresentation errorRepresentation = new JsonRepresentation(error);
			return errorRepresentation;		
			
		}catch (Exception e) {
			e.printStackTrace();
			setStatus(Status.SERVER_ERROR_INTERNAL);
			ErrorResource error = new ErrorResource();
			error.setErrorCode(ErrorResource.INTERNAL_SERVER_ERROR);
			error.setErrorMessage(e.getMessage());
			JsonRepresentation errorRepresentation = new JsonRepresentation(error);
			return errorRepresentation;
			
		}finally {
			log.info("end POST updateDiveSession() DiveSessionUpdateResource)");
			
		}   
		
				
	}
	  
	
}