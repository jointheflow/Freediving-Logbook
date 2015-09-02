package org.gianluca.logbook.rest.resource;



import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.gianluca.logbook.dao.googledatastore.LogbookDAO;
import org.gianluca.logbook.dao.googledatastore.entity.Freediver;
import org.gianluca.logbook.dto.FreediverDto;
import org.gianluca.logbook.external.integration.ExternalUser;
import org.gianluca.logbook.external.integration.ExternalUserFactory;
import org.gianluca.logbook.external.integration.PlatformNotManagedException;
import org.gianluca.logbook.helper.LogbookConstant;
import org.restlet.representation.Representation;
import org.restlet.resource.*; 
import org.restlet.data.Status;
import org.restlet.ext.json.*;

import com.google.appengine.api.datastore.KeyFactory;
import com.restfb.exception.FacebookOAuthException;

public class FreediverLoginResource<K> extends ServerResource{
	private static final Logger log = Logger.getLogger(FreediverLoginResource.class.getName());
	
	
		/*Get Freediver instance basing on externalPlatformId and externalId (may be facebook, google etc.). 
		 * Check the token against external platform.
		 * If the instance does not exist
		  creates new instance* 
		 */
		@Get("json")
		public Representation login(){
			//create json response
			JsonRepresentation representation = null;
			try {
				//get parameter
				//String p_externalId=this.getRequest().getResourceRef().getQueryAsForm().getFirstValue("external_id");
				String p_externalPlatformId= this.getRequest().getResourceRef().getQueryAsForm().getFirstValue("external_platform_id");
				String p_externalToken = this.getRequest().getResourceRef().getQueryAsForm().getFirstValue("external_token");
				
				log.info("start GET login for Freediver");
				//log.info("p_externalId:"+p_externalId);
				log.info("p_externalPlatfomId:"+p_externalPlatformId);
				log.info("p_externalToken:"+p_externalToken);
				
			
				//check token against external platform
				ExternalUser extUser= ExternalUserFactory.createExternalUser(p_externalToken, Integer.parseInt(p_externalPlatformId));
				
				
				//if token ok
				//creates instance
				FreediverDto fdDto = new FreediverDto();
				
				//check if p_externalId exists
				Freediver fd = LogbookDAO.getFreediverByExternalId(extUser.getId(), Integer.parseInt((p_externalPlatformId)));
				
				//if exists return instance
				if (fd!=null) {
					log.info("Freediver found!");
					fdDto.status=LogbookConstant.FREEDIVER_STATUS_OLD;
					
				}
				//else create new instance
				else {
					log.info("Freediver not found!");
					fd = LogbookDAO.addFreediver(extUser.getId(), extUser.getName(), "null", Integer.parseInt(p_externalPlatformId));
					fdDto.status=LogbookConstant.FREEDIVER_STATUS_NEW;
				}
				fdDto.externalId= fd.getExternalId();
				fdDto.externalPlatformId = fd.getExternalPlatformId();
				fdDto.externalToken = p_externalToken;
				fdDto.externalUsername = fd.getExternalName();
				fdDto.id = KeyFactory.keyToString(fd.getId());
				fdDto.deepUnit = fd.getDeepUnit();
				fdDto.temperatureUnit = fd.getTemperatureUnit();
				// find all dive session associated
				
				//add dive session to dto
				
				
				representation= new JsonRepresentation(fdDto);
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
				
			}catch (Exception e) {
				e.printStackTrace();
				setStatus(Status.SERVER_ERROR_INTERNAL);
				ErrorResource error = new ErrorResource();
				error.setErrorCode(ErrorResource.INTERNAL_SERVER_ERROR);
				error.setErrorMessage(e.getMessage());
				JsonRepresentation errorRepresentation = new JsonRepresentation(error);
				return errorRepresentation;
				
			}
			
			finally {
				log.info("end  GET login for Freediver");
				
			}
			
			
		}

	  
	
}
