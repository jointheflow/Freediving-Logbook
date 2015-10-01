package org.gianluca.logbook.rest.resource;


import java.util.logging.Logger;
import org.gianluca.logbook.dao.exception.DiveSessionIdException;
import org.gianluca.logbook.dao.googledatastore.LogbookDAO;
import org.gianluca.logbook.dao.googledatastore.entity.DiveSession;
import org.gianluca.logbook.dto.DiveSessionDto;
import org.gianluca.logbook.dto.DiveSessionInputDto;
import org.gianluca.logbook.dto.LogbookDto;
import org.gianluca.logbook.external.integration.PlatformNotManagedException;
import org.gianluca.logbook.rest.exception.WrongParameterException;
import org.restlet.representation.Representation;
import org.restlet.resource.*; 
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.data.Status;
import org.restlet.ext.json.*;

import com.restfb.exception.FacebookOAuthException;


public class DiveSessionUpdateResource<K> extends ServerResource {
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
	         
	        DiveSessionInputDto dsInputDto = new DiveSessionInputDto();
	        LogbookDtoFactory.populateDiveSessionDtoFromPOSTRequest(dsInputDto, form, LogbookDtoFactory.REQUEST_UPDTAE);
	        
	        
		    //update dive session
		    DiveSession ds = LogbookDAO.updateDiveSession(dsInputDto.id,
		    												dsInputDto.diveDate,
		    												dsInputDto.deep,
		    												dsInputDto.equipment,
		    												dsInputDto.locationDesc,
		    												null,
		    												dsInputDto.meteoDesc,
		    												dsInputDto.note,
		    												dsInputDto.waterTemp,
		    												dsInputDto.weight,
		    												dsInputDto.deepUnit,
		    												dsInputDto.tempUnit,
		    												dsInputDto.weightUnit);
		    
		    //create result dto
		    DiveSessionDto dsDto = new DiveSessionDto();
			LogbookDtoFactory.populateDiveSessionDtoFromEntity(dsDto, ds);
		    //return dive dto
		    
			//Set dto status and message
			dsDto.setExternalToken(dsInputDto.externalToken);
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