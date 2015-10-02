package org.gianluca.logbook.rest.resource;

import java.util.logging.Logger;

import org.gianluca.logbook.dao.exception.FreediverIdException;
import org.gianluca.logbook.dao.googledatastore.LogbookDAO;
import org.gianluca.logbook.dto.FreediverInputDto;
import org.gianluca.logbook.dto.LogbookDto;
import org.gianluca.logbook.external.integration.ExternalUserFactory;
import org.gianluca.logbook.external.integration.PlatformNotManagedException;
import org.gianluca.logbook.rest.exception.WrongParameterException;
import org.restlet.representation.Representation;
import org.restlet.resource.*; 
import org.restlet.data.Parameter;
import org.restlet.data.Status;
import org.restlet.ext.json.*;

import com.restfb.exception.FacebookOAuthException;

public class FreediverGetResource<K> extends ServerResource {
	private static final Logger log = Logger.getLogger(FreediverGetResource.class.getName());
	
	
		
	/*Get a freediver and all dive sessions associated. Check if token is valid against external platform*/
	@Get
	public Representation getFreediver() throws ResourceException {
		try {
			//create json response
			JsonRepresentation representation = null; 
			
	        log.info("start GET getFreediver FreediverGetResource");
			
			 for (Parameter parameter : this.getRequest().getResourceRef().getQueryAsForm()) {
		        	log.info("parameter " + parameter.getName());
		   		  	log.info("/" + parameter.getValue());
		      }	
			         
				
			FreediverInputDto freediverInputDto = LogbookDtoFactory.createFreediverInputDtoFromGETRequest(this.getRequest().getResourceRef().getQueryAsForm(), LogbookDtoFactory.REQUEST_ADD);
			     
	        //check token against external platform
			ExternalUserFactory.checkExternalToken(freediverInputDto.externalToken, freediverInputDto.externalPlatformId);
		    
		    //add dive session
		    LogbookDAO.getFreediverBylId(freediverInputDto.id);
		    LogbookDto resDto = new LogbookDto();
		    
		    //Set dto status and message
			resDto.setResult(LogbookDto.RESULT_OK);
			resDto.setMessage("Freediver got");
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
			
		}catch(FreediverIdException a_e) {
			a_e.printStackTrace();
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
			log.info("end  GET getFreediver FreediverGetResource");
			
		}   
	}
	
}