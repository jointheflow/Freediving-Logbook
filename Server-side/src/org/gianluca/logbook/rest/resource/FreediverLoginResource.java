package org.gianluca.logbook.rest.resource;



import java.util.logging.Logger;

import org.gianluca.logbook.dao.googledatastore.LogbookDAO;

import org.gianluca.logbook.dao.googledatastore.entity.DiveSessionsOfFreeediver;
import org.gianluca.logbook.dao.googledatastore.entity.Freediver;

import org.gianluca.logbook.dto.FreediverDto;
import org.gianluca.logbook.dto.FreediverInputDto;
import org.gianluca.logbook.dto.LogbookDto;
import org.gianluca.logbook.external.integration.ExternalUser;
import org.gianluca.logbook.external.integration.ExternalUserFactory;
import org.gianluca.logbook.external.integration.PlatformNotManagedException;
import org.gianluca.logbook.helper.LogbookConstant;
import org.gianluca.logbook.rest.exception.WrongParameterException;
import org.restlet.representation.Representation;
import org.restlet.resource.*; 
import org.restlet.data.Parameter;
import org.restlet.data.Status;
import org.restlet.ext.json.*;

import com.restfb.exception.FacebookOAuthException;

public class FreediverLoginResource<K>  extends ServerResource {
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
				log.info("start GET login() FreediverLoginResource");
				
				 for (Parameter parameter : this.getRequest().getResourceRef().getQueryAsForm()) {
			        	log.info("parameter " + parameter.getName());
			   		  	log.info("/" + parameter.getValue());
			        }	
			         
				
				FreediverInputDto freediverInputDto = LogbookDtoFactory.createFreediverInputDtoFromGETRequest(this.getRequest().getResourceRef().getQueryAsForm(), LogbookDtoFactory.REQUEST_ADD);
				
					
								
				//check token against external platform
				ExternalUser extUser= ExternalUserFactory.createExternalUser(freediverInputDto.externalToken, freediverInputDto.externalPlatformId);
				
				//check if p_externalId exists
				Freediver fd = LogbookDAO.getFreediverByExternalId(extUser.getId(), freediverInputDto.externalPlatformId);
				String fdStatus=null;
				//if exists return instance and set status to old
				if (fd!=null) {
					log.info("Freediver found!");
					fdStatus = LogbookConstant.FREEDIVER_STATUS_OLD;
				}
				//else create new instance and set status to new
				else {
					log.info("Freediver not found!");
					fd = LogbookDAO.addFreediver(extUser.getId(), extUser.getName(), "null", freediverInputDto.externalPlatformId);
					fdStatus = LogbookConstant.FREEDIVER_STATUS_NEW;
					
				}
				// find all dive session associated
				DiveSessionsOfFreeediver dsOfFree = LogbookDAO.getDiveSessionsByFreediver(fd.getId(), freediverInputDto.pageSize, null);
							
				//creates instance
				FreediverDto fdDto = LogbookDtoFactory.createFreediverDtoFromEntity(fd, dsOfFree, fdStatus, freediverInputDto.externalToken);
								
				//Set dto status and message
				fdDto.setResult(LogbookDto.RESULT_OK);
				fdDto.setMessage("Freediver login executed");
				fdDto.externalToken = freediverInputDto.externalToken;
				
				representation= new JsonRepresentation(fdDto);
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
				
			}
			
			finally {
				log.info("end GET login() FreediverLoginResource");
				
			}
			
			
		}
	
}
