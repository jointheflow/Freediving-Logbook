package org.gianluca.logbook.rest.resource;


import java.util.logging.Logger;

import org.gianluca.logbook.dao.exception.DiveSessionIdException;
import org.gianluca.logbook.dao.googledatastore.LogbookDAO;
import org.gianluca.logbook.dao.googledatastore.entity.Dive;
import org.gianluca.logbook.dto.DiveDto;
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

public class DiveAddResource<K> extends ServerResource implements ILogbookResource{
	private static final Logger log = Logger.getLogger(DiveAddResource.class.getName());
	
	
		
	/*Add a new Dive to a Dive Session . Check if token is valid against external platform*/
	@Post
	public Representation addDive(Representation entity) throws ResourceException {
		
		log.info("start POST addDive() DiveAddResource)");
		//create json response
		JsonRepresentation representation = null;
	    Form form = new Form(entity); 
	   
		try {
			 
	        for (Parameter parameter : form) {
	        	
	        	log.info("parameter " + parameter.getName());
	   		  	log.info("/" + parameter.getValue());
	        }	
	         
	        //check if parameters exists and are valid
		    checkParameters(entity);
		    
	        // retrieves customer parameters  
		    // "name=value"  
	        String externalToken = form.getFirstValue("external_token");
	        String externalPlatformId = form.getFirstValue("external_platform_id");
	        String sessionId = form.getFirstValue("divesession_id");
	        int diveTime = new Integer(form.getFirstValue("dive_time"));
	        int duration = new Integer(form.getFirstValue("duration"));
	        
			Double maxDeep = new Double(form.getFirstValue("max_deep"));
			Double neutralBuoyance = new Double(form.getFirstValue("neutral_buoyance"));
			String equipment = form.getFirstValue("equipment"); 
			String diveType =form.getFirstValue("dive_type");
						
			String note = form.getFirstValue("note");
		    Double weight = new Double(form.getFirstValue("weight"));
		    
		    
		    Double depthWaterTemp = new Double(form.getFirstValue("depth_water_temp"));
		    
		    int deepUnit = Integer.parseInt(form.getFirstValue("deep_unit"));
		    int weightUnit = Integer.parseInt(form.getFirstValue("weight_unit"));
		    int tempUnit = Integer.parseInt(form.getFirstValue("temp_unit"));
		    
		    		   
		    //check token against external platform
			ExternalUserFactory.checkExternalToken(externalToken, Integer.parseInt(externalPlatformId));
		    
		    //add dive session
		    Dive d = LogbookDAO.addDive(sessionId, diveTime, diveType, duration, equipment, maxDeep,neutralBuoyance, note, weight, depthWaterTemp, deepUnit, tempUnit, weightUnit);
		    		 
		    //create result dto
		    DiveDto dDto = new DiveDto();
			dDto.setMaxDeepAsFeet(d.getMaxDeepAsFeet());
			dDto.setMaxDeepAsMeter(d.getMaxDeepAsMeter());
			dDto.setDiveTime(d.getDiveTime());
			dDto.setEquipment(d.getEquipment());
			dDto.setExternalToken(externalToken);
			dDto.setId(d.getId());
			
			dDto.setNote(d.getNote().getValue());
			dDto.setDepthWaterTempAsCelsius(d.getDepthWaterTempAsCelsisus());
			dDto.setDepthWaterTempAsFahrehneit(d.getDepthWaterTempAsfarheneit());
			
			dDto.setWeightAsKilogram(d.getWeightAsKilogram());
			dDto.setWeightAsPound(d.getWeightAsPound());
		    //return dive dto
		    
			//Set dto status and message
			dDto.setResult(LogbookDto.RESULT_OK);
			dDto.setMessage("Dive added");
			
			
			representation= new JsonRepresentation(dDto);
			representation.setIndenting(true);
			
			return representation;
			
		}catch (FacebookOAuthException e_oa) {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			ErrorResource error = new ErrorResource();
			error.setErrorCode(ErrorResource.WRONG_OAUTH_TOKEN);
			error.setErrorMessage(e_oa.getMessage());
			JsonRepresentation errorRepresentation = new JsonRepresentation(error);
			return errorRepresentation;
		}catch (PlatformNotManagedException e_pnm) {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			ErrorResource error = new ErrorResource();
			error.setErrorCode(ErrorResource.PLATFORM_NOT_MANAGED_ERROR);
			error.setErrorMessage(e_pnm.getMessage());
			JsonRepresentation errorRepresentation = new JsonRepresentation(error);
			return errorRepresentation;
		
		}catch (NumberFormatException e_ne) {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			ErrorResource error = new ErrorResource();
			error.setErrorCode(ErrorResource.NUMBER_FORMAT_ERROR);
			error.setErrorMessage(e_ne.getMessage());
			JsonRepresentation errorRepresentation = new JsonRepresentation(error);
			return errorRepresentation;
			
		}catch (WrongParameterException e_wp) {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			ErrorResource error = new ErrorResource();
			error.setErrorCode(ErrorResource.WRONG_PARAMETER_ERROR);
			error.setErrorMessage(e_wp.getMessage());
			JsonRepresentation errorRepresentation = new JsonRepresentation(error);
			return errorRepresentation;	
			
		}catch(DiveSessionIdException a_e) {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			ErrorResource error = new ErrorResource();
			error.setErrorCode(ErrorResource.FREEDIVER_ID_ERROR);
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
			log.info("end POST addDive() DiveAddResource)");
			
		}   
		
				
	}  
	/*Check POST parametes*/
	public void checkParameters(Representation entity) throws WrongParameterException {
		
	
		
		Form form = new Form(entity);
		
		String externalToken = form.getFirstValue("external_token");
		checkExternalToken(externalToken); 
       
		String externalPlatformId = form.getFirstValue("external_platform_id");
		checkExternalPlatformId(externalPlatformId);
		
		String diveId = form.getFirstValue("dive_id");
        checkDiveId(diveId);
        
        
        String maxDeep = form.getFirstValue("max_deep");
        checkDouble(maxDeep, "max_deep");
        
        String s_timeDive = form.getFirstValue("dive_time");
        checkInt(s_timeDive, "dive_time");
        checkTime(new Integer(s_timeDive), "dive_time");
        
        
        String s_duration = form.getFirstValue("duration");
        checkInt(s_duration, "duration");
        checkDuration(new Integer(s_duration), "duration");
        
        
        String waterTemp = form.getFirstValue("depth_water_temp");
	    checkDouble(waterTemp, "depth_water_temp");
	    
        
        String weight = form.getFirstValue("weight");
		checkDouble(weight, "weight");
        
        String diveTime =form.getFirstValue("dive_time");
        if (diveTime==null) throw new WrongParameterException("Parameter dive_time missing");
        //TODO check time
		
		/*String equipment = form.getFirstValue("equipment"); 
		String location = form.getFirstValue("location");
		String meteo = form.getFirstValue("meteo");
		String note = form.getFirstValue("note");
		*/
        
	    String deepUnit = form.getFirstValue("deep_unit");
	    if (deepUnit==null) throw new WrongParameterException("Parameter deep_unit missing");
	    checkInt(deepUnit, "deep_unit");
	    //check if deepUnit is correct value
	    if ((new Integer(deepUnit) < LogbookConstant.DEEP_METER) || (new Integer (deepUnit) > LogbookConstant.DEEP_FEET)) throw new WrongParameterException("Parameter deep_unit wrong value");
	    
	    String weightUnit = form.getFirstValue("weight_unit");
	    if (weightUnit==null) throw new WrongParameterException("Parameter weight_unit missing");
	    checkInt(weightUnit, "weight_unit");
	    //check if weight is correct value
	    if ((new Integer(weightUnit) < LogbookConstant.WEIGHT_KILOGRAM) || (new Integer (weightUnit) > LogbookConstant.WEIGHT_POUND)) throw new WrongParameterException("Parameter weight_unit wrong value");
	    
	    String tempUnit = form.getFirstValue("temp_unit");
	    if (tempUnit==null) throw new WrongParameterException("Parameter temp_unit missing");
	    checkInt(tempUnit, "temp_unit");
	    //check if weight is correct value
	    if ((new Integer(tempUnit) < LogbookConstant.TEMPERATURE_CELSIUS) || (new Integer (tempUnit) > LogbookConstant.TEMPERATURE_FAHRHENEIT)) throw new WrongParameterException("Parameter temp_unit wrong value");   
	    
	    
		
	}
	  
	
}