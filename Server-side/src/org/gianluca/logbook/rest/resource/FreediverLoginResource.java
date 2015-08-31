package org.gianluca.logbook.rest.resource;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.gianluca.logbook.dao.googledatastore.LogbookDAO;
import org.gianluca.logbook.dao.googledatastore.entity.Freediver;
import org.gianluca.logbook.dto.FreediverDto;
import org.gianluca.logbook.helper.PlatformConstant;
import org.restlet.representation.Representation;
import org.restlet.resource.*; 
import org.restlet.data.Status;
import org.restlet.ext.json.*;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import com.restfb.types.User;




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
				String p_externalId=this.getRequest().getResourceRef().getQueryAsForm().getFirstValue("external_id");
				String p_externalPlatformId= this.getRequest().getResourceRef().getQueryAsForm().getFirstValue("external_platform_id");
				String p_externalToken = this.getRequest().getResourceRef().getQueryAsForm().getFirstValue("external_token");
				
				log.info("start GET login for Freediver");
				log.info("p_externalId:"+p_externalId);
				log.info("p_externalPlatfomId:"+p_externalPlatformId);
				log.info("p_externalToken:"+p_externalToken);
				
				//check token against platform
				User fbUser= getFacebookUser(p_externalToken);
				//getHTML();
				
				//if token ok
				//creates instance
				FreediverDto fdDto = new FreediverDto();
				
				//check if p_externalId exists
				
				Freediver fd = LogbookDAO.getFreediverByExternalId(fbUser.getId(), Long.parseLong((p_externalPlatformId)));
				
				//if exists return instance
				if (fd!=null) {
					log.info("Freediver found!");
					fdDto.status=PlatformConstant.FREEDIVER_STATUS_OLD;
					
				}
				//else create new instance
				else {
					log.info("Freediver not found!");
					fd = LogbookDAO.addFreediver(fbUser.getId(), fbUser.getName(), fbUser.getEmail(), Long.parseUnsignedLong(p_externalPlatformId));
					fdDto.status=PlatformConstant.FREEDIVER_STATUS_NEW;
				}
				fdDto.externalId= fd.getExternalId();
				fdDto.externalPlatformId = fd.getExternalPlatformId();
				fdDto.externalToken = p_externalToken;
				fdDto.externalUsername = fd.getExternalName();
				fdDto.id = fd.getId().toString();
				// find all dive session associated
				
				//add dive session to dto
				
				
				representation= new JsonRepresentation(fdDto);
				representation.setIndenting(true);
				
				return representation;
		/*	}catch (WrongUserOrPasswordException e) {
				/*setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				ErrorResource error = new ErrorResource();
				error.setErrorCode(ErrorResource.WRONG_USER_OR_PASSWORD);
				error.setErrorMessage(e.getMessage());
				JsonRepresentation errorRepresentation = new JsonRepresentation(error);
				return errorRepresentation;
				*/
			}	finally {
				log.info("end  GET login for Freediver");
				
			}
			
			
		}

	private User getFacebookUser(String facebookAccessToken) {
		
		FacebookClient facebookClient = new DefaultFacebookClient(facebookAccessToken, "84793f0243f40a9fcf53d4d857e8902d", Version.VERSION_2_4);
		
		return facebookClient.fetchObject("me", User.class);
		
	}

	  
	
}
