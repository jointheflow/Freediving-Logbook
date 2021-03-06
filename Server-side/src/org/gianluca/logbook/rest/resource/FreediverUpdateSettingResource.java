package org.gianluca.logbook.rest.resource;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;


import org.gianluca.logbook.dao.googledatastore.LogbookDAO;
import org.gianluca.logbook.dto.FreediverInputDto;
import org.gianluca.logbook.dto.LogbookDto;
import org.gianluca.logbook.external.integration.ExternalUserFactory;
import org.gianluca.logbook.external.integration.PlatformNotManagedException;
import org.gianluca.logbook.rest.exception.WrongParameterException;
import org.restlet.representation.Representation;
import org.restlet.resource.*; 
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.data.Status;
import org.restlet.ext.json.*;

import com.restfb.exception.FacebookOAuthException;


public class FreediverUpdateSettingResource<K> extends ServerResource {
	private static final Logger log = Logger.getLogger(FreediverUpdateSettingResource.class.getName());
	
	
		
	/*Update the settings for Freediver. Check if token is valid against external platform*/
	@Post
	public Representation updateFreediverSetting(Representation entity) throws ResourceException {
		
		log.info("start POST updateFreediverSetting() FreediverUpdateSettingResource)");
		//create json response
		JsonRepresentation representation = null;
	    Form form = new Form(entity); 
	   
		try {
			 
	        for (Parameter parameter : form) {
	        	
	        	log.info("parameter " + parameter.getName());
	   		  	log.info("/" + parameter.getValue());
	        }	
	         
	        //populate freediver input dto
	        FreediverInputDto freediverInputDto = LogbookDtoFactory.createFreediverInputDtoFromGETRequest(form, LogbookDtoFactory.REQUEST_UPDATE);
		     
	        //check token against external platform
			ExternalUserFactory.checkExternalToken(freediverInputDto.externalToken, freediverInputDto.externalPlatformId);
		    
			//Upadate freediver settings
		    LogbookDAO.updateFreediverPreferences(freediverInputDto.id, new ArrayList<String>(Arrays.asList(freediverInputDto.customFieldListOfDive)));
		    LogbookDto resDto = new LogbookDto();
		    
		    //Set dto status and message
			resDto.setResult(LogbookDto.RESULT_OK);
			resDto.setMessage("Freediver setting updated");
			resDto.setExternalToken(freediverInputDto.externalToken);
		    
		    
			
			representation= new JsonRepresentation(resDto);
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
			
		}catch (Exception e) {
			e.printStackTrace();
			setStatus(Status.SERVER_ERROR_INTERNAL);
			ErrorResource error = new ErrorResource();
			error.setErrorCode(ErrorResource.INTERNAL_SERVER_ERROR);
			error.setErrorMessage(e.getMessage());
			JsonRepresentation errorRepresentation = new JsonRepresentation(error);
			return errorRepresentation;
			
		}finally {
			log.info("end POST updateFreediverSetting() FreediverUpdateSettingResource)");
			
		}   
		
				
	}
	  
	
}