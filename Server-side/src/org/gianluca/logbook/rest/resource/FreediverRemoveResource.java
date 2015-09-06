package org.gianluca.logbook.rest.resource;





import java.util.logging.Logger;

import org.gianluca.logbook.dao.googledatastore.LogbookDAO;

import org.gianluca.logbook.dto.LogbookDto;
import org.gianluca.logbook.external.integration.ExternalUserFactory;
import org.restlet.representation.Representation;
import org.restlet.resource.*; 
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.ext.json.*;

import com.google.appengine.api.datastore.KeyFactory;

public class FreediverRemoveResource<K> extends ServerResource{
	private static final Logger log = Logger.getLogger(FreediverRemoveResource.class.getName());
	
	
		
	/*Remove a freediver and all dive sessions associated. Check if token is valid against external platform*/
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
	        
		    //TODO check if parameters exists and are valid
		     
		   
		    //check token against external platform
			ExternalUserFactory.checkExternalToken(externalToken, Integer.parseInt(externalPlatformId));
		    
		    //add dive session
		    LogbookDAO.removeFreediver(KeyFactory.stringToKey(freediverId));
		    LogbookDto resDto = new LogbookDto();
		    
		  //Set dto status and message
			resDto.setResult(LogbookDto.RESULT_OK);
			resDto.setMessage("Freediver removed");
			resDto.setExternalToken(externalToken);
		    
		    
			
			representation= new JsonRepresentation(resDto);
			representation.setIndenting(true);
			
			return representation;
			
		}catch (Exception e) {
			//TODO manage exception
			e.printStackTrace();
			//TODO return error representation
			return null;
			
		}finally {
			log.info("end  POST remove for freediver");
			
		}   
	}  
	 
		
	  
	
}