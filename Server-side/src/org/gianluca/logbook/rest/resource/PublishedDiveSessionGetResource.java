package org.gianluca.logbook.rest.resource;

import java.util.logging.Logger;



import org.gianluca.logbook.dao.exception.DiveSessionIdException;
import org.gianluca.logbook.dao.googledatastore.LogbookDAO;
import org.gianluca.logbook.dao.googledatastore.entity.DiveSession;
import org.gianluca.logbook.dto.DiveInputDto;
import org.gianluca.logbook.dto.DiveSessionDto;
import org.gianluca.logbook.dto.LogbookDto;
import org.gianluca.logbook.rest.exception.WrongParameterException;
import org.restlet.representation.Representation;
import org.restlet.resource.*; 
import org.restlet.data.Parameter;
import org.restlet.data.Status;
import org.restlet.ext.json.*;


public class PublishedDiveSessionGetResource<K> extends ServerResource {
	private static final Logger log = Logger.getLogger(PublishedDiveSessionGetResource.class.getName());
	
	
		
	/*Get all dives of published session*/
	@Get
	public Representation getPublishedDiveSession(Representation entity) throws ResourceException {
		log.info("start  GET getPublishedDiveSession");
		//create json response
		JsonRepresentation representation = null;
	     
		try {
			 
	        for (Parameter parameter : this.getRequest().getResourceRef().getQueryAsForm()) {
	        	log.info("parameter " + parameter.getName());
	   		  	log.info("/" + parameter.getValue());
	        }	 
	        
	        //create dive input dto from request
	        DiveInputDto diveInputDto = LogbookDtoFactory.createDiveInputDtoFromPOSTRequest(this.getRequest().getResourceRef().getQueryAsForm(), LogbookDtoFactory.REQUEST_GET_PUBLISHED);
	        
	        
		    //add dive session
		    DiveSession ds = LogbookDAO.getDiveSession(diveInputDto.diveSessionId);
		    //Complete population
		    DiveSessionDto diveSessionDto = LogbookDtoFactory.createDiveSessionDtoFromEntity(ds);
		    
		    //Set dto status and message
		    LogbookDto lDto = new LogbookDto();
		    
		    lDto.setExternalToken(diveInputDto.externalToken);
		    lDto.setResult(LogbookDto.RESULT_OK);
		    lDto.setMessage("Dive session found");
		    lDto.setDetail(diveSessionDto);
		    
			representation= new JsonRepresentation(lDto);
			representation.setIndenting(true);
			
			return representation;
			
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
			
		} catch (DiveSessionIdException e) {
			e.printStackTrace();
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			ErrorResource error = new ErrorResource();
			error.setErrorCode(ErrorResource.DIVESESSION_ID_ERROR);
			error.setErrorMessage(e.getMessage());
			JsonRepresentation errorRepresentation = new JsonRepresentation(error);
			return errorRepresentation;	
		}
		catch (Exception e) {
			e.printStackTrace();
			setStatus(Status.SERVER_ERROR_INTERNAL);
			ErrorResource error = new ErrorResource();
			error.setErrorCode(ErrorResource.INTERNAL_SERVER_ERROR);
			error.setErrorMessage(e.getMessage());
			JsonRepresentation errorRepresentation = new JsonRepresentation(error);
			return errorRepresentation;
			
					
		}finally {
			log.info("end GET getPublishedDiveSession");
			
		}   
	}
	
}