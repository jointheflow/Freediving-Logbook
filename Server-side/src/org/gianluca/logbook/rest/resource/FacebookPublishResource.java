package org.gianluca.logbook.rest.resource;


import java.net.URLEncoder;
import java.util.logging.Logger;

import org.gianluca.logbook.dao.exception.DiveSessionIdException;
import org.gianluca.logbook.dao.googledatastore.LogbookDAO;
import org.gianluca.logbook.dao.googledatastore.entity.DiveSession;
import org.gianluca.logbook.dto.DiveSessionDto;
import org.gianluca.logbook.dto.DiveSessionInputDto;
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

public class FacebookPublishResource<K> extends ServerResource {
	private static final Logger log = Logger.getLogger(FacebookPublishResource.class.getName());
	
	
		
	/*Publish a dive session to facebook. Check if token is valid against external platform*/
	@Post
	public Representation publishFacebook(Representation entity) throws ResourceException {
		
		log.info("start POST publishFacebook() FacebookPublishResource");
		//create json response
		JsonRepresentation representation = null;
	    Form form = new Form(entity); 
	   
		try {
			 
	        for (Parameter parameter : form) {
	        	
	        	log.info("parameter " + parameter.getName());
	   		  	log.info("/" + parameter.getValue());
	        }	
	        
	        
	        //create divesession input dto from request
	        //DiveInputDto diveInputDto = LogbookDtoFactory.createDiveInputDtoFromPOSTRequest(form, LogbookDtoFactory.REQUEST_GET);
	        DiveSessionInputDto diveSessionInputDto = LogbookDtoFactory.createDiveSessionInputDtoFromPOSTRequest(form, LogbookDtoFactory.REQUEST_FB_PUBLISH);
	        
		    //get dive session in a lazy manner (only dive session info)
		    DiveSession ds = LogbookDAO.getDiveSessionLazy(diveSessionInputDto.id);
		    //Complete population
		    DiveSessionDto diveSessionDto = LogbookDtoFactory.createDiveSessionDtoFromEntity(ds);
		    //TODO: may i check if the dive session is associated with the user????
		    
		    //build publication url
		    String publicationUrlAndParams = LogbookConstant.FACEBOOK_SAMPLE_PUB_URL+"?divesessionID="+diveSessionInputDto.id+
		    																		"&userName="+URLEncoder.encode(diveSessionInputDto.userName, "UTF-8")+
		    																		"&location="+URLEncoder.encode(diveSessionInputDto.locationDesc,"UTF-8")+
		    																		"&maxDepth="+URLEncoder.encode(diveSessionInputDto.maxDiveDepth.toString(), "UTF-8")+
		    																		"&maxDuration="+URLEncoder.encode(diveSessionInputDto.maxDiveDuration.toString(), "UTF-8");
		    
		    //TODO: must decide if pass here all dive session information for publication
		    //as parameters to a target JSP or a target JSP retrieve all info needed (may be best the second because..)
		    
		    
		    //invoking publishing method
		    String messageId= ExternalUserFactory.facebookPublishDiveSession(diveSessionInputDto.externalToken, publicationUrlAndParams);
		    
		    //Set dto status and message
		    LogbookDto lDto = new LogbookDto();
		    
		    
		    //TODO: set message for publication
		    lDto.setExternalToken(diveSessionInputDto.externalToken);
		    lDto.setResult(LogbookDto.RESULT_OK);
		    lDto.setMessage("Dive session published on facebook - messageID:"+messageId);
		    lDto.setDetail(diveSessionDto);
		    
			representation= new JsonRepresentation(lDto);
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
			log.info("end POST publishFacebook() FacebookPublishResource");
			
		}   
		
				
	}  
	
	
}