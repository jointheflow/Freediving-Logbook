package org.gianluca.logbook.rest.resource;




import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import org.gianluca.logbook.dao.exception.FreediverIdException;
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


public class DiveSessionAddResource<K> extends ServerResource implements ILogbookResource{
	private static final Logger log = Logger.getLogger(DiveSessionAddResource.class.getName());
	
	
		
	/*Add a new DiveSession for a freediver. Check if token is valid against external platform*/
	@Post
	public Representation addDiveSession(Representation entity) throws ResourceException {
		
		log.info("start POST addDiveSession() DiveSessionAddResource)");
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
	        String freediverId = form.getFirstValue("freediver_id");
	        String s_diveDate = form.getFirstValue("dive_date");
	        SimpleDateFormat formatter = new SimpleDateFormat(LogbookConstant.DATE_FORMAT);
		    Date diveDate = formatter.parse(s_diveDate);
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
		    
		    		   
		    //check token against external platform
			ExternalUserFactory.checkExternalToken(externalToken, Integer.parseInt(externalPlatformId));
		    
		    //add dive session
		    DiveSession ds = LogbookDAO.addDiveSession(freediverId, diveDate, deep, equipment , location, null, meteo, note, waterTemp, weight, deepUnit, tempUnit, weightUnit);
		    
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
			dsDto.setMessage("Dive session added");
			
			
			representation= new JsonRepresentation(dsDto);
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
			
		}catch(FreediverIdException a_e) {
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
			log.info("end POST addDiveSession() DiveSessionAddResource)");
			
		}   
		
				
	}  
	/*Check POST parametes*/
	public void checkParameters(Representation entity) throws WrongParameterException {
		Form form = new Form(entity);
		
		String externalToken = form.getFirstValue("external_token");
		checkExternalToken(externalToken); 
       
		String externalPlatformId = form.getFirstValue("external_platform_id");
		checkExternalPlatformId(externalPlatformId);
		
		String freediverId = form.getFirstValue("freediver_id");
        checkFreediverId(freediverId);
        
        
        String deep = form.getFirstValue("deep");
        checkDouble(deep, "deep");
        
        String waterTemp = form.getFirstValue("water_temp");
	    checkDouble(waterTemp, "water_temp");
	    
        String weight = form.getFirstValue("weight");
		checkDouble(weight, "weight");
        
        String diveDate =form.getFirstValue("dive_date");
        if (diveDate==null) throw new WrongParameterException("Parameter dive_date missing");
        checkDate(diveDate, "dive_date");
		
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